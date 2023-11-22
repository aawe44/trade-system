package com.jason.trade.lightning.deal.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.trade.common.service.LimitBuyService;
import com.jason.trade.common.utils.SnowflakeIdWorker;
import com.jason.trade.common.utils.RedisWorker;
import com.jason.trade.lightning.deal.client.GoodsFeignClient;
import com.jason.trade.lightning.deal.client.model.Goods;
import com.jason.trade.lightning.deal.client.model.Order;
import com.jason.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import com.jason.trade.lightning.deal.mq.OrderMessageSender;
import com.jason.trade.lightning.deal.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SeckillActivityServiceImpl implements SeckillActivityService {

    @Autowired
    private SeckillActivityDao seckillActivityDao;
    @Autowired
    private RedisWorker redisWorker;

    @Autowired
    private LimitBuyService limitBuyService;

    private final SnowflakeIdWorker snowFlake = new SnowflakeIdWorker(6, 8);

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    @Override
    public boolean insertSeckillActivity(SeckillActivity seckillActivity) {
        return seckillActivityDao.insertSeckillActivity(seckillActivity);
    }

    @Override
    public SeckillActivity querySeckillActivityById(long id) {
        return seckillActivityDao.querySeckillActivityById(id);
    }

    @Override
    public List<SeckillActivity> queryActivitysByStatus(int status) {
        return seckillActivityDao.querySeckillActivityByStatus(status);
    }

    /**
     * Process basic seckill requests.
     * Handles potential overselling in high concurrency scenarios.
     *
     * @param seckillActivityId Seckill activity ID
     * @return Whether the operation was successful
     */
    @Override
    public boolean processSeckillReqBase(long seckillActivityId) {
        // Retrieve information about the seckill activity
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);

        // Check if the seckill activity exists
        if (seckillActivity == null) {
            log.error("Seckill activity ID = {} not found for corresponding seckill activity", seckillActivityId);
            throw new RuntimeException("Unable to find corresponding seckill activity");
        }

        int availableStock = seckillActivity.getAvailableStock();

        // Perform the seckill operation
        if (availableStock > 0) {
            log.info("Product successfully purchased during seckill");
            seckillActivityDao.updateAvailableStockByPrimaryKey(seckillActivityId);
            return true;
        } else {
            log.info("Seckill failed, product is sold out");
            return false;
        }
    }

    /**
     * Process seckill requests using a Redis Lua script for inventory deduction validation.
     *
     * @param userId            User ID
     * @param seckillActivityId Seckill activity ID
     * @return The created order if the operation was successful
     * @throws RuntimeException If the user is not eligible, stock is insufficient, or other failures occur
     */
    @Override
    public Order processSeckill(long userId, long seckillActivityId) {

        // 1. Check if the user is eligible to purchase
        if (limitBuyService.isInLimitMember(seckillActivityId, userId)) {
            log.error("User has already purchased and cannot buy again. seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("User has already purchased and cannot buy again");
        }

        // 2. Use a Redis Lua script for inventory deduction validation
        String stockKey = "stock:" + seckillActivityId;
        boolean stockCheckResult = redisWorker.stockDeductCheck(stockKey);
        if (!stockCheckResult) {
            log.error("Insufficient stock. seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("Insufficient stock, seckill failed");
        }

        // 3. Retrieve information for the corresponding seckill activity
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        if (seckillActivity == null) {
            log.error("Seckill activity not found for ID = {}", seckillActivityId);
            throw new RuntimeException("Unable to find corresponding seckill activity");
        }

        // 4. Lock the stock to prevent multiple purchases
        boolean lockStockResult = seckillActivityDao.lockStock(seckillActivityId);
        if (!lockStockResult) {
            log.info("Seckill failed, product sold out. seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("Seckill failed, product sold out");
        }
        log.info("Seckill successful. seckillActivityId={} userId={}", seckillActivityId, userId);

        // 5. Create an order and send a message to create the order
        Order order = buildOrder(userId, seckillActivity);

        // Send a message to create the order
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;
    }

    /**
     * Build an Order based on seckill activity details.
     *
     * @param userId          User ID
     * @param seckillActivity Seckill activity details
     * @return The created order
     */
    private Order buildOrder(long userId, SeckillActivity seckillActivity) {
        return Order.builder()
                .id(snowFlake.nextId())
                .activityId(seckillActivity.getId())
                // Type 1 indicates a seckill activity
                .activityType(1)
                .goodsId(seckillActivity.getGoodsId())
                .payPrice(seckillActivity.getSeckillPrice())
                .userId(userId)
                .status(1)
                .createTime(new Date())
                .build();
    }

    /**
     * Locks stock for a seckill activity.
     *
     * @param seckillActivityId Seckill activity ID
     * @return True if the stock is successfully locked, false otherwise
     */
    @Override
    public boolean lockStock(long seckillActivityId) {
        log.info("Locking stock for seckill activity. seckillActivityId: {}", seckillActivityId);
        return seckillActivityDao.lockStock(seckillActivityId);
    }

    /**
     * Deducts stock for a seckill activity.
     *
     * @param seckillActivityId Seckill activity ID
     * @return True if the stock is successfully deducted, false otherwise
     */
    @Override
    public boolean deductStock(long seckillActivityId) {
        log.info("Deducting stock for seckill activity. seckillActivityId: {}", seckillActivityId);
        return seckillActivityDao.deductStock(seckillActivityId);
    }

    /**
     * Reverts stock for a seckill activity.
     *
     * @param seckillActivityId Seckill activity ID
     * @return True if the stock is successfully reverted, false otherwise
     */
    @Override
    public boolean revertStock(long seckillActivityId) {
        log.info("Reverting stock for seckill activity. seckillActivityId: {}", seckillActivityId);
        return seckillActivityDao.revertStock(seckillActivityId);
    }

    @Override
    public void pushSeckillActivityInfoToCache(long id) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(id);
        // Stock information
        redisWorker.setValue("stock:" + id, Long.valueOf(seckillActivity.getAvailableStock()));

        // Complete activity information
        redisWorker.setValue("seckillActivity:" + seckillActivity.getId(), JSON.toJSONString(seckillActivity));

        // Information about the goods associated with the activity
        Goods goods = goodsFeignClient.queryGoodsById(seckillActivity.getGoodsId());
        redisWorker.setValue("seckillActivity_goods:" + seckillActivity.getGoodsId(), JSON.toJSONString(goods));
    }
}

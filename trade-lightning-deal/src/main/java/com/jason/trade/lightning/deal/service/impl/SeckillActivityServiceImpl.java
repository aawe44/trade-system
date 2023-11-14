package com.jason.trade.lightning.deal.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import com.jason.trade.lightning.deal.service.SeckillActivityService;
import com.jason.trade.order.db.model.Order;
import com.jason.trade.order.db.model.OrderStatus;
import com.jason.trade.order.mq.OrderMessageSender;
import com.jason.trade.order.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jason.trade.lightning.deal.utils.RedisWorker;
import com.jason.trade.order.service.LimitBuyService;

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
     * Process seckill requests using Redis Lua script for inventory deduction validation.
     *
     * @param seckillActivityId Seckill activity ID
     * @return Whether the operation was successful
     */
    @Override
    public Order processSeckill(long userId, long seckillActivityId) {

        //1.校验用户是否有购买资格
        if (limitBuyService.isInLimitMember(seckillActivityId, userId)) {
            log.error("当前用户已经购买过不能重复购买 seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("当前用户已经购买过,不能重复购买");
        }

        //2.使用Redis中Lua先进行库存校验
        String key = "stock:" + seckillActivityId;
        boolean checkResult = redisWorker.stockDeductCheck(key);
        if (!checkResult) {
            log.error("库存不足 seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("库存不足，抢购失败");
        }

        //3.查询对应的秒杀活动信息
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);
        if (seckillActivity == null) {
            log.error("Seckill activity ID = {} not found for corresponding seckill activity", seckillActivityId);
            throw new RuntimeException("Unable to find corresponding seckill activity");
        }

        //4.锁定库存
        boolean lockStockResult = seckillActivityDao.lockStock(seckillActivityId);
        if (!lockStockResult) {
            log.info("商品抢购失败，商品已经售完 seckillActivityId={} userId={}", seckillActivityId, userId);
            throw new RuntimeException("商品抢购失败，商品已经售完");
        }
        log.info("商品抢购成功 seckillActivityId={} userId={}", seckillActivityId, userId);


        Order order = Order.builder()
                .id(snowFlake.nextId())
                .activityId(seckillActivityId)
                //type=1表示秒杀活动
                .activityType(1)
                .goodsId(seckillActivity.getGoodsId())
                .payPrice(seckillActivity.getSeckillPrice())
                .userId(userId)
                .status(OrderStatus.AWAITING_ORDER.getCode())
                .createTime(new Date())
                .build();

        //5.创建订单，发送创建订单消息
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;

    }

    @Override
    public boolean lockStock(long id) {
        log.info("秒杀活动锁定库存 seckillActivityId:{}", id);
        return seckillActivityDao.lockStock(id);

    }

    @Override
    public boolean deductStock(long id) {
        log.info("秒杀活动扣减库存 seckillActivityId:{}", id);
        return seckillActivityDao.deductStock(id);
    }

    @Override
    public boolean revertStock(long id) {
        log.info("秒杀活动回补库存 seckillActivityId:{}", id);
        return seckillActivityDao.revertStock(id);
    }

}

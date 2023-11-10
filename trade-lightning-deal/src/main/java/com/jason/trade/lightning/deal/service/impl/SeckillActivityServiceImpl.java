package com.jason.trade.lightning.deal.service.impl;

import com.jason.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import com.jason.trade.lightning.deal.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jason.trade.lightning.deal.utils.RedisWorker;

import java.util.List;

@Service
@Slf4j
public class SeckillActivityServiceImpl implements SeckillActivityService {

    @Autowired
    private SeckillActivityDao seckillActivityDao;
    @Autowired
    private RedisWorker redisWorker;

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
    public boolean processSeckill(long seckillActivityId) {
        // Build the Redis key
        String key = "stock:" + seckillActivityId;

        // Use a Lua script to validate inventory deduction
        boolean checkResult = redisWorker.stockDeductCheck(key);
        if (!checkResult) {
            return false; // Insufficient stock, seckill failed
        }

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

}

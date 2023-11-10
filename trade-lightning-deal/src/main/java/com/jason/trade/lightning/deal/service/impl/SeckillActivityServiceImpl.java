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
     * 处理秒杀请求
     * 高并发时会出现超卖
     *
     * @param seckillActivityId
     * @return
     */
    @Override
    public boolean processSeckillReqBase(long seckillActivityId) {
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);

        if (seckillActivity == null) {
            log.error("seckillActivityId = {} 查詢不到對應的秒殺活動", seckillActivityId);
            throw new RuntimeException("查詢不到對應的秒殺活動");
        }
        int availableStock = seckillActivity.getAvailableStock();
        if (availableStock > 0) {
            log.info("商品搶購成功");
            seckillActivityDao.updateAvailableStockByPrimaryKey(seckillActivityId);
            return true;
        } else {
            log.info("商品搶購失敗，商品已經售完");
            return false;
        }

    }


    /**
     * 处理秒杀请求
     * 通过Redis Lua脚本先进行校验
     *
     * @param seckillActivityId
     * @return
     */
    @Override
    public boolean processSeckill(long seckillActivityId) {
        String key = "stock:" + seckillActivityId;
        boolean checkResult = redisWorker.stockDeductCheck(key);
        if (!checkResult) {
            return false;
        }

        //1.查询对应的秒杀活动信息
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);

        if (seckillActivity == null) {
            log.error("seckillActivityId={} 查询不到对应的秒杀活动", seckillActivityId);
            throw new RuntimeException("查询不到对应的秒杀活动");
        }
        int availableStock = seckillActivity.getAvailableStock();
        if (availableStock > 0) {
            log.info("商品抢购成功");
            seckillActivityDao.updateAvailableStockByPrimaryKey(seckillActivityId);
            return true;
        } else {
            log.info("商品抢购失败，商品已经售完");
            return false;
        }
    }
}

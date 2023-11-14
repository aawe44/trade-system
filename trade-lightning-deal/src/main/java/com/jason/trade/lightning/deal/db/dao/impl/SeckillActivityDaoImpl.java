package com.jason.trade.lightning.deal.db.dao.impl;

import com.jason.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.jason.trade.lightning.deal.db.mappers.SeckillActivityMapper;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SeckillActivityDaoImpl implements SeckillActivityDao {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Override
    public boolean insertSeckillActivity(SeckillActivity seckillActivity) {
        int result = seckillActivityMapper.insert(seckillActivity);
        return result > 0;
    }

    @Override
    public SeckillActivity querySeckillActivityById(long id) {
        return seckillActivityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SeckillActivity> querySeckillActivityByStatus(int status) {
        return seckillActivityMapper.queryActivitysByStatus(status);
    }

    @Override
    public boolean updateAvailableStockByPrimaryKey(long seckillActivityId) {
        int result = seckillActivityMapper.updateAvailableStockByPrimaryKey(seckillActivityId);
        return result > 0;
    }

    @Override
    public boolean lockStock(long id) {
        int result = seckillActivityMapper.lockStock(id);
        //大于0 表示插入成功
        if (result < 0) {
            log.error("锁定库存失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean deductStock(long id) {
        int result = seckillActivityMapper.deductStock(id);
        //大于0 表示插入成功
        if (result < 1) {
            log.error("扣减库存失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean revertStock(long id) {
        int result = seckillActivityMapper.revertStock(id);
        if (result < 1) {
            log.error("库存回补失败");
            return false;
        }
        return true;
    }
}

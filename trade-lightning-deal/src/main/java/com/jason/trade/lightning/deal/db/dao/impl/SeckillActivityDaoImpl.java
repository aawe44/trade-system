package com.jason.trade.lightning.deal.db.dao.impl;

import com.jason.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.jason.trade.lightning.deal.db.mappers.SeckillActivityMapper;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return  seckillActivityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SeckillActivity> querySeckillActivityByStatus(int status) {
        return null;
    }
}

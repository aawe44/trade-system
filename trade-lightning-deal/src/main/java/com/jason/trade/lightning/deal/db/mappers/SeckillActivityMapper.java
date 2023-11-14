package com.jason.trade.lightning.deal.db.mappers;

import com.jason.trade.lightning.deal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivity record);

    int insertSelective(SeckillActivity record);

    SeckillActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivity record);

    int updateByPrimaryKey(SeckillActivity record);

    List<SeckillActivity> queryActivitysByStatus(int status);

    int updateAvailableStockByPrimaryKey(long seckillActivityId);

    int lockStock(Long id);

    int deductStock(Long id);

    int revertStock(Long id);
}
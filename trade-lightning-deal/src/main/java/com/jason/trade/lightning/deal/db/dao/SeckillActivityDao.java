package com.jason.trade.lightning.deal.db.dao;

import com.jason.trade.lightning.deal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityDao {
    boolean insertSeckillActivity(SeckillActivity seckillActivity);

    SeckillActivity querySeckillActivityById(long id);

    List<SeckillActivity> querySeckillActivityByStatus(int status);

    boolean updateAvailableStockByPrimaryKey(long seckillActivityId);

    boolean lockStock(long id);

    boolean deductStock(long id);

    boolean revertStock(long id);
}

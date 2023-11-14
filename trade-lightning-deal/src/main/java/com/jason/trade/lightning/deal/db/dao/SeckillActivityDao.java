package com.jason.trade.lightning.deal.db.dao;

import com.jason.trade.lightning.deal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityDao {
    boolean insertSeckillActivity(SeckillActivity seckillActivity);

    SeckillActivity querySeckillActivityById(long id);

    List<SeckillActivity> querySeckillActivityByStatus(int status);

    boolean updateAvailableStockByPrimaryKey(long seckillActivityId);

    /**
     * 锁定秒杀的库存
     *
     * @param id
     * @return
     */
    boolean lockStock(long id);

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    boolean deductStock(long id);

    /**
     * 锁定的库存回补
     *
     * @param id
     * @return
     */
    boolean revertStock(long id);
}

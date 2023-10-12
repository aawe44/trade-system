package com.jason.trade.goods.db.dao;

import com.jason.trade.goods.db.model.Goods;

public interface GoodsDao {

    boolean insertGoods(Goods goods);

    boolean deleteGoods(long id);

    Goods queryGoodsById(long id);

    boolean updateGoods(Goods goods);


}

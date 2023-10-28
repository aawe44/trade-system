package com.jason.trade.goods.service;

import com.jason.trade.goods.db.model.Goods;

public interface GoodsService {
    boolean insertGoods(Goods goods);

    Goods queryGoodsById(long id);

    boolean lockStock(long goodsId);
}

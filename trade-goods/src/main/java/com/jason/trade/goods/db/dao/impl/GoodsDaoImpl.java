package com.jason.trade.goods.db.dao.impl;

import com.jason.trade.goods.db.dao.GoodsDao;
import com.jason.trade.goods.db.mappers.GoodsMapper;
import com.jason.trade.goods.db.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public boolean insertGoods(Goods goods) {
        int result = goodsMapper.insert(goods);
        return result > 0;
    }

    @Override
    public boolean deleteGoods(long id) {
        int result = goodsMapper.deleteByPrimaryKey(id);
        return result > 0;
    }

    @Override
    public Goods queryGoodsById(long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateGoods(Goods goods) {
        int result = goodsMapper.updateByPrimaryKey(goods);
        return result > 0;
    }
}

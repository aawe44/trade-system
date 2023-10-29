package com.jason.trade.goods.db.dao.impl;

import com.jason.trade.goods.db.dao.GoodsDao;
import com.jason.trade.goods.db.mappers.GoodsMapper;
import com.jason.trade.goods.db.model.Goods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
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

    @Override
    public boolean lockStock(long goodsId) {
        int result = goodsMapper.lockStock(goodsId);
        if (result < 0) {
            log.error("Failed to lock stock");
            return false;
        }
        return true;
    }

    @Override
    public boolean deductStock(Long goodsId) {
        int result = goodsMapper.deductStock(goodsId);
        if (result < 1) {
            log.error("Failed to deduct stock");
            return false;
        }
        return true;
    }

    @Override
    public boolean revertStock(Long goodsId) {
        int result = goodsMapper.revertStock(goodsId);
        if (result < 1) {
            log.error("Failed to revert stock");
            return false;
        }
        return true;
    }
}

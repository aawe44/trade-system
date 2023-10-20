package com.jason.trade.goods;

import com.alibaba.fastjson.JSON;
import com.jason.trade.goods.db.dao.GoodsDao;
import com.jason.trade.goods.db.model.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsTest {

    @Autowired
    private GoodsDao goodsDao;

    @Test
    public void insertGoodsTest() {
        System.out.println("Hello");
        Goods goods = new Goods();
        goods.setTitle("iphone 15");
        goods.setBrand("Apple");
        goods.setCategory("cellphone");
        goods.setNumber("NO123456");
        goods.setImage("test");
        goods.setDescription("iphone 15 pro max is very good");
        goods.setKeywords("cellphone apple iphone15 iphone");
        goods.setSaleNum(0);
        goods.setLockStock(10000);
        goods.setPrice(999999);
        goods.setStatus(1);
        goods.setAvailableStock(5);
        boolean insertResult = goodsDao.insertGoods(goods);
        System.out.println(insertResult);

    }

    @Test
    public void deleteGoodsTest() {
        boolean deleteResult = goodsDao.deleteGoods(1);
        System.out.println(deleteResult);
    }

    @Test
    public void queryGoodsTest() {
        Goods goods = goodsDao.queryGoodsById(2);
        System.out.println(JSON.toJSONString(goods));
    }

    @Test
    public void updateGoods() {
        Goods goods = goodsDao.queryGoodsById(16);
        goods.setTitle(goods.getTitle() + " update");
        goodsDao.updateGoods(goods);
    }


}

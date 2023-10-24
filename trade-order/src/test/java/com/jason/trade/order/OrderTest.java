package com.jason.trade.order;

import com.jason.trade.order.db.dao.OrderDao;
import com.jason.trade.order.db.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    // This test method demonstrates inserting an order into the system.
    public void insertGoodsTest() {
        Order order = buildOrder(123456L, 123L, new Date(), 39900);

        boolean insertResult = orderDao.insertOrder(order);

        System.out.println(insertResult);
    }

    // Helper method to build an Order object with the provided data.
    private Order buildOrder(long userId, long goodsId, Date payTime, int payPrice) {
        return Order.builder().userId(userId).goodsId(goodsId).payTime(payTime).payPrice(payPrice).build();
    }
}

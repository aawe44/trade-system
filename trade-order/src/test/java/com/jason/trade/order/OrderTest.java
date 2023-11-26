package com.jason.trade.order;

import com.jason.trade.common.utils.SnowflakeIdWorker;
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
    private SnowflakeIdWorker snowFlake = new SnowflakeIdWorker(6, 8);

    @Test
    // This test method demonstrates inserting an order into the system.
    public void insertGoodsTest() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.println("Hello");
                Order order = new Order();
//            order.setId(snowFlake.nextId() + 1);
                order.setId(1L + 10 * i + j);
                order.setUserId(1L + i);
                order.setGoodsId(12378L);
                order.setPayTime(new Date());
                order.setPayPrice(1999);
                order.setStatus(1);
                order.setActivityType(1);
                order.setCreateTime(new Date());
                boolean insertresult = orderDao.insertOrder(order);
                System.out.println(insertresult);
            }
        }
    }

}

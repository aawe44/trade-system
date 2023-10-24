package com.jason.trade.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.trade.goods.db.dao.GoodsDao;
import com.jason.trade.goods.db.model.Goods;
import com.jason.trade.order.db.dao.OrderDao;
import com.jason.trade.order.db.model.Order;
import com.jason.trade.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
// This class implements the OrderService interface and provides order-related functionality.
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    // Creates a new order for a user and a product.
    public Order createOrder(long userId, long goodsId) {

        // Build the order object with initial data.
        Order order = Order.builder()
                .activityId(0L)
                .activityType(0)
                .userId(userId)
                .goodsId(goodsId)
                .status(1)
                .createTime(new Date())
                .build();

        // Retrieve product information from the goods service.
        Goods goods = goodsDao.queryGoodsById(goodsId);

        // Check if the product exists.
        if (goods == null) {
            log.error("Goods is null, goodsId={}, userId={}", goodsId, userId);
            return null;
        }

        // Set the order's pay price based on the product's price.
        order.setPayPrice(goods.getPrice());

        // Insert the order into the database.
        boolean result = orderDao.insertOrder(order);

        // Check if the order insertion was successful.
        if (!result) {
            log.error("Order insert error, order={}", JSON.toJSONString(order));
            return null;
        }

        // Return the created order.
        return order;
    }

    @Override
    // Retrieves an order by its unique ID.
    public Order queryOrder(long orderId) {
        return null; // To be implemented.
    }

    @Override
    // Processes the payment for an order with the given ID.
    public void payOrder(long orderId) {
        // To be implemented.
    }
}

package com.jason.trade.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.trade.goods.db.dao.GoodsDao;
import com.jason.trade.goods.db.model.Goods;
import com.jason.trade.goods.service.GoodsService;
import com.jason.trade.order.db.dao.OrderDao;
import com.jason.trade.order.db.model.Order;
import com.jason.trade.order.db.model.OrderStatus;
import com.jason.trade.order.mq.OrderMessageSender;
import com.jason.trade.order.service.OrderService;
import com.jason.trade.order.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
// This class implements the OrderService interface and provides order-related functionality.
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsDao goodsDao;

    private SnowflakeIdWorker snowFlake = new SnowflakeIdWorker(6, 8);

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Autowired
    private GoodsService goodsService;

    /**
     * 创建订单和库存锁定在一个事务中，要么同时成功，要么同时失败
     * 使用 @Transactional(rollbackFor = Exception.class)
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    // Creates a new order for a user and a product.
    public Order createOrder(long userId, long goodsId) {

        // Build the order object with initial data.
        Order order = Order.builder()
                .id(snowFlake.nextId())
                .activityId(0L)
                .activityType(0)
                .goodsId(goodsId)
                .userId(userId)
                .status(OrderStatus.AWAITING_ORDER.getCode())
                .createTime(new Date())
                .build();

        // 1. Check if the product exists.
        // Retrieve product information from the goods service.
        Goods goods = goodsDao.queryGoodsById(goodsId);

        if (goods == null) {
            log.error("Goods is null, goodsId={}, userId={}", goodsId, userId);
            return null;
        }

        if (goods.getAvailableStock() <= 0) {
            log.error("goods stock not enough goodsId={}, userId={}", goodsId, userId);
            throw new RuntimeException("商品庫存不足");
        }

        boolean lockResult = goodsService.lockStock(goodsId);


        // 4. Create order
        // Set the order's pay price based on the product's price.
        order.setPayPrice(goods.getPrice());

        // Insert the order into the database.
        boolean insertResult = orderDao.insertOrder(order);

        // Check if the order insertion was successful.
        if (!insertResult) {
            log.error("Order insert error, order={}", JSON.toJSONString(order));
            return null;
        }

        // 5. 發送訂單支付狀態檢查消息
        orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));

        // Return the created order.
        return order;
    }

    @Override
    // Retrieves an order by its unique ID.
    public Order queryOrder(long orderId) {
        return orderDao.queryOrderById(orderId);
    }

    @Override
// Processes payment for an order with the given ID.
    public void payOrder(long orderId) {
        log.info("Paying order, Order ID: {}", orderId);

        Order order = orderDao.queryOrderById(orderId);

        if (order == null) {
            log.error("Order ID={}, Order does not exist", orderId);
            return;
        }

        int orderStatus = order.getStatus(); // Store the order status in a variable for clarity

        if (orderStatus != OrderStatus.AWAITING_ORDER.getCode()) {
            log.error("Order ID={}, Order status cannot be paid", orderId);
            return;
        }

        log.info("Initiating payment through a third-party payment platform...");

        order.setPayTime(new Date());

        order.setStatus(OrderStatus.COMPLETED_ORDER.getCode());
        orderDao.updateOrder(order);
    }

}

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
     * Creates an order and locks the corresponding inventory in a single transaction,
     * ensuring either both succeed or both fail.
     * Utilizes @Transactional(rollbackFor = Exception.class).
     *
     * @param userId   The ID of the user placing the order.
     * @param goodsId  The ID of the product being ordered.
     * @return The created Order object or null if unsuccessful.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
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
            log.error("Goods stock not enough goodsId={}, userId={}", goodsId, userId);
            throw new RuntimeException("Insufficient product inventory");
        }

        // Attempt to lock the product's stock.
        boolean lockResult = goodsService.lockStock(goodsId);
        if (!lockResult) {
            log.error("Order stock lock error. Order: {}", JSON.toJSONString(order));
            throw new RuntimeException("Failed to lock product inventory for the order");
        }

        // Set the order's payment price based on the product's price.
        order.setPayPrice(goods.getPrice());

        // 4. Create the order and send a message to create the order.
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;
    }


    @Override
    // Retrieves an order by its unique ID.
    public Order queryOrder(long orderId) {
        return orderDao.queryOrderById(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    // Processes payment for an order with the given ID.
    public void payOrder(long orderId) {
        log.info("Paying order, Order ID: {}", orderId);
        // Step 1: Retrieve the order
        Order order = orderDao.queryOrderById(orderId);
        if (order == null) {
            log.error("Order ID={}, Order does not exist", orderId);
            throw new RuntimeException("Order does not exist");
        }

        // Step 2: Check if the order can be paid
        int orderStatus = order.getStatus(); // Store the order status in a variable for clarity
        if (orderStatus != OrderStatus.AWAITING_ORDER.getCode()) {
            log.error("Order ID={}, Order status cannot be paid", orderId);
            throw new RuntimeException("Order status cannot be paid");
        }

        log.info("Initiating payment through a third-party payment platform...");

        // Step 3: Update order details
        order.setPayTime(new Date());

        order.setStatus(OrderStatus.COMPLETED_ORDER.getCode());
        boolean updateResult = orderDao.updateOrder(order);
        if (!updateResult) {
            log.error("Order ID={} - Order payment status update failed", orderId);
            throw new RuntimeException("Order payment status update failed");
        }

        //库存扣减
        boolean deductResult = goodsService.deductStock(order.getGoodsId());
        if (!deductResult) {
            log.error("Order ID={} - Stock deduction failed", orderId);
            throw new RuntimeException("Stock deduction failed");
        }
    }

}

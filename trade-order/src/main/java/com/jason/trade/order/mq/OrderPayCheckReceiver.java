package com.jason.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.jason.trade.order.client.GoodsFeignClient;
import com.jason.trade.order.db.dao.OrderDao;
import com.jason.trade.order.db.model.Order;
import com.jason.trade.order.db.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderPayCheckReceiver {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    @RabbitListener(queues = "order.pay.status.check.queue")
    public void process(String message) {
        // Log the time of reception and the message content
        log.info("Received at: {} | Message: {}", LocalDateTime.now(), message);

        Order order = JSON.parseObject(message, Order.class);

        //Only process orders for normal (non-special) products.
        if (order.getActivityType() != 0) {
            return;
        }

        // 1. Query order information
        Order orderInfo = orderDao.queryOrderById(order.getId());

        // 2. Check if the order is awaiting payment
        if (orderInfo.getStatus() == OrderStatus.AWAITING_ORDER.getCode()) {

            log.info("Order {} is closed due to timeout.", order.getId());

            // 3. Update the order status to "CLOSED"
            orderInfo.setStatus(OrderStatus.ORDER_CLOSED_TIMEOUT.getCode());
            orderDao.updateOrder(orderInfo);

            // 4. Revert the locked stock
            goodsFeignClient.revertStock(orderInfo.getGoodsId());

        }
    }
}

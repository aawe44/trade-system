package com.jason.trade.order.mq;

import com.alibaba.fastjson.JSON;
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


    @RabbitListener(queues = "order.pay.status.check.queue")
    public void process(String message) {
        // Log the time of reception and the message content
        log.info("Received at: {} | Message: {}", LocalDateTime.now(), message);

        Order order = JSON.parseObject(message, Order.class);

        // Check if the order is still in the 'created' status
        Order orderInfo = orderDao.queryOrderById(order.getId());
        if (orderInfo.getStatus() == OrderStatus.AWAITING_ORDER.getCode()) {

            log.info("Order {} is closed due to timeout.", order.getId());

            // Update the order status to 'closed' if it's still in the 'created' status
            orderInfo.setStatus(OrderStatus.ORDER_CLOSED_TIMEOUT.getCode());
            orderDao.updateOrder(orderInfo);
        }
    }
}

package com.jason.trade.order.mq;

import com.alibaba.fastjson.JSON;
import com.jason.trade.order.db.dao.OrderDao;
import com.jason.trade.order.db.model.Order;

import com.jason.trade.order.service.LimitBuyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateOrderReceiver {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Autowired
    private LimitBuyService limitBuyService;

    /**
     * Handles messages related to order creation.
     *
     * @param message The incoming message containing order information.
     */
    @RabbitListener(queues = "create.order.queue")
    public void process(String message) {
        log.info("Processing order creation message. Received content: {}", message);

        // Deserialize the message into an Order object
        Order order = JSON.parseObject(message, Order.class);

        // Step 1: Generate the order
        boolean orderInsertionResult = orderDao.insertOrder(order);
        if (!orderInsertionResult) {
            log.error("Failed to insert order. Details: {}", JSON.toJSONString(order));
            throw new RuntimeException("Failed to generate the order");
        }

        // Step 2: Send a message to check the payment status of the order
        orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));

        //3.判断如果是秒杀活动，加入限购名单
        if (order.getActivityType() == 1) {
            limitBuyService.addLimitMember(order.getActivityId(), order.getUserId());
        }

    }
}

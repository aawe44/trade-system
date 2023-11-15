package com.jason.trade.order.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderMessageSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * Sends a delayed message for order payment status check.
     *
     * @param message The message to send.
     */
    public void sendPayStatusCheckDelayMessage(String message) {
        // Log the message being sent
        log.info("Sending order creation completion and payment status confirmation message: {}", message);

        // Use the AmqpTemplate to send the message to the specified exchange and routing key
        amqpTemplate.convertAndSend("order-event-exchange", "order.create", message);
    }

    /**
     * Sends a message to create an order.
     *
     * @param message The content of the message containing order information.
     */
    public void sendCreateOrderMessage(String message) {
        log.info("Sending create order message: {}", message);
        amqpTemplate.convertAndSend("order-event-exchange", "to.create.order", message);
    }

    /**
     * Send a message for a successful payment of a seckill order.
     *
     * @param message The message to be sent.
     */
    public void sendSeckillPaySucessMessage(String message) {
        log.info("Sending seckill order payment success message:{}", message);
        amqpTemplate.convertAndSend("order-event-exchange", "seckill.order.pay.success", message);
    }


}

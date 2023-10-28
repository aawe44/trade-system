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
}

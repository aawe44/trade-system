package com.jason.trade.order.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderPayCheckReceiver {

    @RabbitListener(queues = "order.pay.status.check.queue")
    public void process(String message) {
        // Log the time of reception and the message content
        log.info("Received at: {} | Message: {}", LocalDateTime.now(), message);
    }
}

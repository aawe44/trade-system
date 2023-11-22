package com.jason.trade.lightning.deal.mq;

import com.alibaba.fastjson.JSON;
import com.jason.trade.lightning.deal.client.model.Order;
import com.jason.trade.lightning.deal.service.SeckillActivityService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeckillPaySucessReceiver {

    @Autowired
    private SeckillActivityService seckillActivityService;

    /**
     * Handles messages for successful seckill payments.
     *
     * @param message The received message
     */
    @RabbitListener(queues = "seckill.order.pay.success.queue")
    public void process(String message) {
        try {
            log.info("Processing seckill payment success message: {}", message);
            Order order = JSON.parseObject(message, Order.class);

            // Deduct stock for the seckill activity
            seckillActivityService.deductStock(order.getActivityId());

            log.info("Seckill payment successful for order {}", order.getId());
        } catch (Exception e) {
            log.error("Error processing seckill payment success message: {}", e.getMessage(), e);
        }
    }
}

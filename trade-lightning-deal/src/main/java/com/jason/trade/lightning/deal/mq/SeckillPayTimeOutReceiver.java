package com.jason.trade.lightning.deal.mq;

import com.alibaba.fastjson.JSON;
import com.jason.trade.common.service.LimitBuyService;
import com.jason.trade.lightning.deal.client.OrderFeignClient;
import com.jason.trade.lightning.deal.client.model.Order;
import com.jason.trade.lightning.deal.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SeckillPayTimeOutReceiver {

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private LimitBuyService limitBuyService;

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * Handles messages for seckill payment timeout.
     *
     * @param message The received message
     */
    @RabbitListener(queues = "seckill.order.pay.status.check.queue")
    public void process(String message) {

        try {
            log.info("Processing seckill payment timeout message: {}", message);
            Order receivedOrder = JSON.parseObject(message, Order.class);

            // Only process seckill orders
            if (receivedOrder.getActivityType() != 1) {
                log.info("Non-seckill order received. Skipping processing.");
                return;
            }

            // Retrieve order information from the database
            Order existingOrder = orderFeignClient.queryOrderById(receivedOrder.getId());

            // Check if the order status is 'waiting for payment'
            if (existingOrder.getStatus() == 1) {
                // 1. Remove user from the limit purchase list
                limitBuyService.removeLimitMember(existingOrder.getActivityId(), existingOrder.getUserId());

                // 2. Revert seckill stock
                seckillActivityService.revertStock(existingOrder.getActivityId());

                log.info("Order {} payment timed out. Closing the order.", existingOrder.getId());

                // 3. Update the order status to closed
                existingOrder.setStatus(99);
                orderFeignClient.updateOrder(existingOrder);
            }

        } catch (Exception e) {
            log.error("Error processing seckill payment timeout message: {}", e.getMessage(), e);
        }
    }
}

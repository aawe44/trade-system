package com.jason.trade.order.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Configuration class for setting up RabbitMQ message queues.
 */
@Configuration
public class RabbitMqConfig {

    /**
     * Defines the order delay queue, which serves as a dead-letter queue for delayed orders.
     *
     * @return The configured order delay queue.
     */
    @Bean
    public Queue orderDelayQueue() {
        // Configure queue properties
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.pay.status.check");
        arguments.put("x-message-ttl", 60000);

        // Create and return the queue
        return new Queue("order.delay.queue", true, false, false, arguments);
    }

    /**
     * Defines the order release queue for checking the payment status of orders.
     *
     * @return The configured order release queue.
     */
    @Bean
    public Queue orderReleaseQueue() {
        // Create and return the queue
        return new Queue("order.pay.status.check.queue", true, false, false);
    }

    /**
     * Defines the order event exchange for handling order-related events.
     *
     * @return The configured order event exchange.
     */
    @Bean
    public Exchange orderEventExchange() {
        // Create and return the topic exchange
        return new TopicExchange("order-event-exchange", true, false);
    }

    /**
     * Binds the order delay queue to the order event exchange for order creation events.
     *
     * @return The binding configuration.
     */
    @Bean
    public Binding orderCreateBinding() {
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create",  // Routing key typically represents the event name
                null);
    }

    /**
     * Binds the order release queue to the order event exchange for order status check events.
     *
     * @return The binding configuration.
     */
    @Bean
    public Binding orderReleaseBinding() {
        return new Binding("order.pay.status.check.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.pay.status.check",
                null);
    }

    /**
     * Creates the order queue (regular queue).
     *
     * @return A Queue object representing the "create.order.queue" with specified properties.
     */
    @Bean
    public Queue createOrderQueue() {
        Queue queue = new Queue("create.order.queue", true, false, false);
        return queue;
    }

    /**
     * Binds the order status verification queue to the exchange.
     * The order status verification queue is bound to the "order-event-exchange."
     *
     * @return A Binding object defining the connection between the queue and the exchange.
     */
    @Bean
    public Binding createOrderBinding() {
        return new Binding("create.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "to.create.order",
                null);
    }


    /**
     * 秒杀订单支付成功消息
     *
     * @return
     */
    @Bean
    public Queue seckillPaySucessQueue() {
        Queue queue = new Queue("seckill.order.pay.success.queue", true, false, false);
        return queue;
    }

    /**
     * 秒杀订单支付成功 绑定
     * 秒杀订单支付成功队列 绑定到交换机
     *
     * @return
     */
    @Bean
    public Binding seckillPaySucessBinding() {
        return new Binding("seckill.order.pay.success.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "seckill.order.pay.success",
                null);
    }


    /**
     * 秒杀订单支付状态检查消息
     *
     * @return
     */
    @Bean
    public Queue seckillPayTimeOutCheQueue() {
        Queue queue = new Queue("seckill.order.pay.status.check.queue", true, false, false);
        return queue;
    }

    /**
     * 秒杀订单支付超时 绑定
     * 秒杀订单支付超时队列 绑定到交换机
     *
     * @return
     */
    @Bean
    public Binding seckillPayTimeOutBinding() {
        return new Binding("seckill.order.pay.status.check.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.pay.status.check",
                null);
    }

}

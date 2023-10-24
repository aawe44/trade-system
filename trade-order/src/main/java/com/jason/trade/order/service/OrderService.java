package com.jason.trade.order.service;

import com.jason.trade.order.db.model.Order;

// This package contains service interfaces related to order management.

public interface OrderService {

    /**
     * Creates an order for a given user and product.
     *
     * @param userId   The ID of the user placing the order.
     * @param goodsId  The ID of the product being ordered.
     * @return         The created Order object representing the new order.
     */
    Order createOrder(long userId, long goodsId);

    /**
     * Retrieves an order by its unique ID.
     *
     * @param orderId  The unique ID of the order to retrieve.
     * @return         The Order object representing the retrieved order.
     */
    Order queryOrder(long orderId);

    /**
     * Processes the payment for an order with the given ID.
     *
     * @param orderId  The unique ID of the order to be paid for.
     */
    void payOrder(long orderId);
}

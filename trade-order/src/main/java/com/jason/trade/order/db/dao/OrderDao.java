package com.jason.trade.order.db.dao;

import com.jason.trade.order.db.model.Order;

// This package contains DAO (Data Access Object) classes for interacting with the database.

public interface OrderDao {

    // Inserts an order into the database and returns a boolean indicating success.
    boolean insertOrder(Order order);

    // Deletes an order by its ID and returns a boolean indicating success.
    boolean deleteOrder(long id);

    // Queries an order by its ID and returns the corresponding Order object.
    Order queryOrderById(long id);

    // Updates an existing order in the database and returns a boolean indicating success.
    boolean updateOrder(Order order);
}

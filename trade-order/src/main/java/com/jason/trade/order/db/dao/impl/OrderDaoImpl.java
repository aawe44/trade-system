package com.jason.trade.order.db.dao.impl;

import com.jason.trade.order.db.dao.OrderDao;
import com.jason.trade.order.db.mappers.OrderMapper;
import com.jason.trade.order.db.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// This package contains the implementations of DAO (Data Access Object) classes.

@Repository
public class OrderDaoImpl implements OrderDao {


    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean insertOrder(Order order) {
        // Inserts an order using the OrderMapper and checks if the result is greater than 0.
        int result = orderMapper.insert(order);
        return result > 0;
    }

    @Override
    public boolean deleteOrder(long id) {
        // Deletes an order by ID using the OrderMapper and checks if the result is greater than 0.
        int result = orderMapper.deleteByPrimaryKey(id);
        return result > 0;
    }

    @Override
    public Order queryOrderById(long id) {
        // Queries an order by ID using the OrderMapper.
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateOrder(Order order) {
        // Updates an existing order using the OrderMapper and checks if the result is greater than 0.
        int result = orderMapper.updateByPrimaryKey(order);
        return result > 0;
    }
}

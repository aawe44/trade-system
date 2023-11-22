package com.jason.trade.order.controller;

import com.alibaba.fastjson.JSON;
import com.jason.trade.order.db.model.Order;
import com.jason.trade.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order/createOrder")
    @ResponseBody
    public Order createOrder(long userId, long goodsId) {
        log.info("createOrder userId:{} goodsId:{}", userId, goodsId);
        return orderService.createOrder(userId, goodsId);
    }

    @GetMapping("/order/queryOrder")
    @ResponseBody
    public Order queryOrder(long orderId) {
        log.info("queryOrder orderId:{}", orderId);
        return orderService.queryOrder(orderId);
    }

    @GetMapping("/order/payOrder")
    @ResponseBody
    public void payOrder(long orderId) {
        log.info("payOrder orderId:{}", orderId);
        orderService.payOrder(orderId);
    }

    @PostMapping("/order/updateOrder")
    @ResponseBody
    public boolean updateOrder(@RequestBody Order order) {
        log.info("updateOrder order:{}", JSON.toJSON(order));
        return orderService.updateOrder(order);
    }
}

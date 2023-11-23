package com.jason.trade.web.manager.client;


import com.jason.trade.web.manager.client.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//FeignClient注解设置服务提供者名字，在consul注册中心注册的服务名字
@FeignClient(name = "trade-order", contextId = "trade-web-manager")
public interface OrderFeignClient {

    @RequestMapping("/order/createOrder")
    Order createOrder(@RequestParam("userId") long userId, @RequestParam("goodsId") long goodsId);

    @RequestMapping("/order/queryOrder")
    Order queryOrder(@RequestParam("orderId") long orderId);

    @RequestMapping("/order/payOrder")
    void payOrder(@RequestParam("orderId") long orderId);

}

package com.jason.trade.lightning.deal.client;

import com.jason.trade.lightning.deal.client.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//FeignClient注解设置服务提供者名字，在consul注册中心注册的服务名字
@FeignClient(name = "trade-order", contextId = "trade-web-portal")
public interface OrderFeignClient {

    @RequestMapping("/order/createOrder")
    Order createOrder(@RequestParam("userId") long userId, @RequestParam("goodsId") long goodsId);

    @RequestMapping("/order/queryOrder")
    Order queryOrderById(@RequestParam("orderId") long orderId);

    @RequestMapping("/order/payOrder")
    void payOrder(@RequestParam("orderId") long orderId);

    /**
     * 修改对应的订单信息
     *
     * @param order
     * @return
     */
    @RequestMapping("/order/updateOrder")
    boolean updateOrder(@RequestBody Order order);
}

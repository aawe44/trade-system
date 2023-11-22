package com.jason.trade.lightning.deal.client;

import com.jason.trade.lightning.deal.client.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


//FeignClient name注解设置服务提供者名字，在consul注册中心注册的服务名字
@FeignClient(name = "trade-goods", contextId = "trade-lightning-deal")
public interface GoodsFeignClient {


    /**
     * 查询商品信息
     * <p>
     * 定义服务提供者接口
     * 参数：要和服务提供者对应
     * 方法名：不强制要求和服务提供者对应
     * 返回值：要和服务提供者对应
     * URL：和服务提供者的访问路径对应
     *
     * @param id
     * @return
     */
    @RequestMapping("/goods/queryGoodsById")
    Goods queryGoodsById(@RequestParam("id") long id);

    /**
     * 锁定商品的库存
     * <p>
     * 定义服务提供者接口
     * 参数：要和服务提供者对应
     * 方法名：不强制要求和服务提供者对应
     * 返回值：要和服务提供者对应
     * URL：和服务提供者的访问路径对应
     *
     * @param id
     * @return
     */
    @RequestMapping("/goods/lockStock")
    boolean lockStock(@RequestParam("id") long id);

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    @RequestMapping("/goods/deductStock")
    boolean deductStock(@RequestParam("id") long id);

    /**
     * 锁定的库存回补
     *
     * @param id
     * @return
     */
    @RequestMapping("/goods/revertStock")
    boolean revertStock(@RequestParam("id") long id);


    /**
     * 根据关键词搜索
     *
     * @param keyword
     * @param from
     * @param size
     * @return
     */
    @RequestMapping("/goods/searchGoodsList")
    List<Goods> searchGoodsList(@RequestParam("keyword") String keyword, @RequestParam("from") int from, @RequestParam("size") int size);
}

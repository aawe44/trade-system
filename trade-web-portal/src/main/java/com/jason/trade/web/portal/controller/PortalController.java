package com.jason.trade.web.portal.controller;


import com.alibaba.fastjson.JSON;
import com.jason.trade.common.model.TradeResultDTO;
import com.jason.trade.common.utils.RedisWorker;
import com.jason.trade.web.portal.client.GoodsFeignClient;
import com.jason.trade.web.portal.client.OrderFeignClient;
import com.jason.trade.web.portal.client.SeckillActivityFeignClient;
import com.jason.trade.web.portal.client.model.Goods;
import com.jason.trade.web.portal.client.model.Order;
import com.jason.trade.web.portal.client.model.SeckillActivity;
import com.jason.trade.web.portal.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PortalController {


    @Autowired
    private GoodsFeignClient goodsFeignClient;


    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private SeckillActivityFeignClient seckillActivityFeignClient;


    @Autowired
    private RedisWorker redisWorker;

    /**
     * Display the goods_detail page.
     *
     * @return The view name for the goods_detail page.
     */
    @RequestMapping("/goods_detail")
    public String index() {
        return "goods_detail";
    }

    /**
     * Display details of a specific goods item.
     *
     * @param goodsId The ID of the goods to display.
     * @return A ModelAndView containing goods details.
     */
    @RequestMapping("/goods/{goodsId}")
    public ModelAndView itemPage(@PathVariable long goodsId) {
        Goods goods = goodsFeignClient.queryGoodsById(goodsId);
        log.info("goodsId={},goods={}", goodsId, JSON.toJSON(goods));
        String showPrice = CommonUtils.changeF2Y(goods.getPrice());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("goods", goods);
        modelAndView.addObject("showPrice", showPrice);
        modelAndView.setViewName("goods_detail");
        return modelAndView;
    }


    /**
     * Display the search page.
     *
     * @return The view name for the search page.
     */
    @RequestMapping("/search")
    public String searchPage() {
        return "search";
    }

    /**
     * Perform a search action based on user input.
     *
     * @param searchWords The search keywords entered by the user.
     * @param resultMap   The map to store search results for rendering in the view.
     * @return The view name for displaying search results.
     */
    @RequestMapping("/searchAction")
    public String search(@RequestParam("searchWords") String searchWords, Map<String, Object> resultMap) {

        log.info("search searchWords:{}", searchWords);
        List<Goods> goodsList = goodsFeignClient.searchGoodsList(searchWords, 0, 10);
        resultMap.put("goodsList", goodsList);
        return "search";

    }

    /**
     * Initiate the purchase process for a specific goods item.
     *
     * @param userId  The ID of the user making the purchase.
     * @param goodsId The ID of the goods to be purchased.
     * @return A ModelAndView for the purchase process.
     */
    @RequestMapping("/buy/{userId}/{goodsId}")
    public ModelAndView buy(@PathVariable long userId, @PathVariable long goodsId) {
        // Create a ModelAndView with the view name "buy_result" to be returned later
        ModelAndView modelAndView = new ModelAndView("buy_result");

        try {
            // Log the start of the order creation process
            log.info("Attempting to create an order for userId={}, goodsId={}", userId, goodsId);

            // Attempt to create an order using the provided userId and goodsId
            Order order = orderFeignClient.createOrder(userId, goodsId);

            // If the order is successfully created, add it to the model and set a success message
            modelAndView.addObject("order", order);
            modelAndView.addObject("resultInfo", "Order placed successfully");

        } catch (Exception e) {
            // If an exception occurs during the order creation process, log the error and handle it
            log.error("Failed to create an order. Error message: {}", e.getMessage());

            // Add an error message to the model to inform the user about the failure
            modelAndView.addObject("errorInfo", "Order placement failed. Reason: " + e.getMessage());
            modelAndView.setViewName("error");
        }

        // Return the ModelAndView, which will render the "buy_result" view with the appropriate data
        return modelAndView;
    }


    /**
     * Handles the request to query an order by its ID.
     *
     * @param resultMap A map for storing result data to be displayed in the view.
     * @param orderId   The ID of the order to be queried.
     * @return The view name for displaying the order details.
     */
    @RequestMapping("/order/query/{orderId}")
    public String orderQuery(Map<String, Object> resultMap, @PathVariable long orderId) {
        // Query the order using the provided orderId
        Order order = orderFeignClient.queryOrder(orderId);

        // Log information about the order and its JSON representation
        log.info("orderId={}, order={}", orderId, JSON.toJSON(order));

        // Convert the order's payPrice to a user-friendly format
        String orderShowPrice = CommonUtils.changeF2Y(order.getPayPrice());

        // Add order and orderShowPrice to the resultMap to be displayed in the view
        resultMap.put("order", order);
        resultMap.put("orderShowPrice", orderShowPrice);

        // Return the view name for displaying the order details
        return "order_detail";
    }


    /**
     * Handles the request to pay for an order with the given ID.
     *
     * @param resultMap A map for storing result data to be displayed in the view.
     * @param orderId   The ID of the order to be paid.
     * @return The view name for displaying the order details or an error view in case of an exception.
     * @throws Exception If an error occurs during the payment process.
     */
    @RequestMapping("/order/payOrder/{orderId}")
    public String payOrder(Map<String, Object> resultMap, @PathVariable long orderId) throws Exception {
        try {
            // Attempt to process the payment for the order with the provided orderId
            orderFeignClient.payOrder(orderId);

            // If payment is successful, redirect to the order details page
            return "redirect:/order/query/" + orderId;
        } catch (Exception e) {
            // If an exception occurs during payment, log the error and display an error view
            log.error("payOrder error, errorMessage: {}", e.getMessage());
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        }
    }

    /**
     * Display details of a seckill activity.
     *
     * @param resultMap A map for storing result data.
     * @param seckillId The ID of the seckill activity.
     * @return The view name for the seckill details page.
     */
    @RequestMapping("/seckill/{seckillId}")
    public String showSeckillInfo(Map<String, Object> resultMap, @PathVariable long seckillId) {
        try {
            // Retrieve seckill activity information
            SeckillActivity seckillActivity = getSeckillActivity(seckillId);

            if (seckillActivity == null) {
                log.error("Failed to find seckill activity information for seckillId: {}", seckillId);
                throw new RuntimeException("Failed to find seckill activity information");
            }

            log.info("Seckill details - seckillId: {}, seckillActivity: {}", seckillId, JSON.toJSON(seckillActivity));

            // Format prices
            String seckillPrice = CommonUtils.changeF2Y(seckillActivity.getSeckillPrice());
            String oldPrice = CommonUtils.changeF2Y(seckillActivity.getOldPrice());

            // Retrieve goods information
            Goods goods = getGoods(seckillActivity.getGoodsId());

            if (goods == null) {
                log.error("Failed to find goods information for seckillId: {}, goodsId: {}", seckillId, seckillActivity.getGoodsId());
                throw new RuntimeException("Failed to find goods information");
            }

            // Populate resultMap with relevant information
            resultMap.put("seckillActivity", seckillActivity);
            resultMap.put("seckillPrice", seckillPrice);
            resultMap.put("oldPrice", oldPrice);
            resultMap.put("goods", goods);

            return "seckill_item";
        } catch (Exception e) {
            log.error("Failed to get seckill info details. Error message: {}", e.getMessage());
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        }
    }

    // Helper method to retrieve seckill activity information from cache or database
    private SeckillActivity getSeckillActivity(long seckillId) {
        String seckillActivityInfo = redisWorker.getValueByKey("seckillActivity:" + seckillId);
        if (!StringUtils.isEmpty(seckillActivityInfo)) {
            // Cache hit
            SeckillActivity seckillActivity = JSON.parseObject(seckillActivityInfo, SeckillActivity.class);
            log.info("Cache hit for seckill activity: {}", seckillActivityInfo);
            return seckillActivity;
        } else {
            // Cache miss, query from the database
            log.info("Cache miss for seckill activity, querying from the database. seckillId: {}", seckillId);
            return seckillActivityFeignClient.querySeckillActivityById(seckillId);
        }
    }

    // Helper method to retrieve goods information from cache or database
    private Goods getGoods(long goodsId) {
        String goodsInfo = redisWorker.getValueByKey("seckillActivity_goods:" + goodsId);
        if (!StringUtils.isEmpty(goodsInfo)) {
            // Cache hit
            Goods goods = JSON.parseObject(goodsInfo, Goods.class);
            log.info("Cache hit for goods: {}", goodsInfo);
            return goods;
        } else {
            // Cache miss, query from the database
            log.info("Cache miss for goods, querying from the database. goodsId: {}", goodsId);
            return goodsFeignClient.queryGoodsById(goodsId);
        }
    }


    /**
     * Retrieve a list of active seckill activities.
     *
     * @param resultMap A map for storing result data.
     * @return The view name for the seckill activity list.
     */
    @RequestMapping("/seckill/list")
    public String activityList(Map<String, Object> resultMap) {
        try {
            List<SeckillActivity> seckillActivities = seckillActivityFeignClient.queryActivitysByStatus(1);
            resultMap.put("seckillActivities", seckillActivities);
            return "seckill_activity_list";
        } catch (Exception e) {
            log.error("Failed to retrieve seckill activity list. Error message: {}", e.getMessage());
            resultMap.put("errorInfo", e.getMessage());
            return "error";
        }
    }

//    @ResponseBody
//    @RequestMapping("/seckill/buy/{seckillId}")
//    public String seckillInfo(@PathVariable long seckillId) {
//        // Uncomment the appropriate line based on the seckill processing method
//        // boolean success = seckillActivityService.processSeckillReqBase(seckillId);
//        boolean success = seckillActivityService.processSeckill(seckillId);
//
//        if (success) {
//            return "Seckill successful! Congratulations on your purchase!";
//        } else {
//            return "Seckill unsuccessful. The product is sold out.";
//        }
//    }

    @ResponseBody
    @RequestMapping("/seckill/buy/{userId}/{seckillId}")
    public ModelAndView seckillInfo(@PathVariable long userId, @PathVariable long seckillId) {

        ModelAndView modelAndView = new ModelAndView();
        try {
            TradeResultDTO<Order> orderTradeResultDTO = seckillActivityFeignClient.processSeckill(userId, seckillId);
            log.info("seckillActivityFeignClient.processSeckill  result:{}", orderTradeResultDTO);
            if (orderTradeResultDTO.getCode() != 200) {
                throw new RuntimeException(orderTradeResultDTO.getErrorMessage());
            }
            modelAndView.addObject("resultInfo", "秒杀抢购成功");
            modelAndView.addObject("order", orderTradeResultDTO.getData());
            modelAndView.setViewName("buy_result");
        } catch (Exception e) {
            log.error("seckill buy error", e);
            modelAndView.addObject("errorInfo", e.getMessage());
            modelAndView.setViewName("error");
        }

        return modelAndView;

    }

    /**
     * 秒杀活动详情页
     *
     * @param seckillId
     * @return
     */
    @RequestMapping("/seckill/static/{seckillId}")
    public String seckillInfo(@PathVariable long seckillId) {
        long startTime = System.nanoTime();
        try {
            return "seckill_item_" + seckillId;
        } finally {
            long endTime = System.nanoTime();
            log.info("static method process time {}", endTime - startTime);
        }
    }


}

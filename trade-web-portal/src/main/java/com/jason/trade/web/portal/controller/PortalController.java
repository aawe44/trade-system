package com.jason.trade.web.portal.controller;


import com.alibaba.fastjson.JSON;
import com.jason.trade.goods.db.model.Goods;
import com.jason.trade.goods.service.GoodsService;
import com.jason.trade.goods.service.impl.SearchServiceImpl;
import com.jason.trade.order.db.model.Order;
import com.jason.trade.order.service.OrderService;
import com.jason.trade.web.portal.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PortalController {


    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SearchServiceImpl searchService;

    @Autowired
    private OrderService orderService;

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
        Goods goods = goodsService.queryGoodsById(goodsId);
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
        List<Goods> goodsList = searchService.searchGoodsList(searchWords, 0, 10);
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
    public String buy(Map<String, Object> resultMap, @PathVariable long userId, @PathVariable long goodsId) {
        // Log information about the request.
        log.info("userId={}, goodsId={}", userId, goodsId);

        // Create an order based on the provided user and product IDs.
        Order order = orderService.createOrder(userId, goodsId);

        // Populate the result map with order information and a success message.
        resultMap.put("order", order);
        resultMap.put("resultInfo", "Order placed successfully");

        // Return the view name "buy_result" to display the result.
        return "buy_result";
    }

}

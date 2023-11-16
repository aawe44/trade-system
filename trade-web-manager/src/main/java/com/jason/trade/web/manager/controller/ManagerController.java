package com.jason.trade.web.manager.controller;


import com.jason.trade.goods.db.model.Goods;
import com.jason.trade.goods.service.GoodsService;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import com.jason.trade.lightning.deal.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
public class ManagerController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillActivityService seckillActivityService;


    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/add_goods")
    public String addGoods() {
        return "add_goods";
    }

    @RequestMapping("/addGoodsAction")
    public String addGoodsAction(
            @RequestParam("title") String title,
            @RequestParam("number") String number,
            @RequestParam("brand") String brand,
            @RequestParam("image") String image,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam("keywords") String keywords,
            @RequestParam("category") String category,
            @RequestParam("stock") int stock, Map<String, Object> resultMap
    ) {
        Goods goods = new Goods();
        goods.setTitle(title);
        goods.setNumber(number);
        goods.setBrand(brand);
        goods.setImage(image);
        goods.setDescription(description);
        goods.setPrice(price);
        goods.setKeywords(keywords);
        goods.setCategory(category);
        goods.setAvailableStock(stock);
        goods.setStatus(1);
        goods.setSaleNum(0);
        goods.setCreateTime(new Date());
        boolean result = goodsService.insertGoods(goods);
        log.info("add goods result={}", result);
        resultMap.put("goodsInfo", goods);
        return "add_goods";

    }

    /**
     * Redirects to the page for adding a seckill activity.
     *
     * @return The view name for the seckill activity page.
     */
    @RequestMapping("/addSkillActivity")
    public String addSkillActivity() {
        return "add_skill_activity";
    }

    /**
     * Handles the action to add a new seckill activity.
     *
     * @param activityName   The name of the seckill activity.
     * @param goodsId        The ID of the associated goods.
     * @param startTime      The start time of the seckill activity.
     * @param endTime        The end time of the seckill activity.
     * @param availableStock The available stock for the seckill activity.
     * @param seckillPrice   The seckill price.
     * @param oldPrice       The original price.
     * @param resultMap      A map for storing result data.
     * @return The view name after adding the seckill activity.
     */
    @RequestMapping("/addSkillActivityAction")
    public String addSkillActivityAction(
            @RequestParam("activityName") String activityName,
            @RequestParam("goodsId") long goodsId,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("availableStock") int availableStock,
            @RequestParam("seckillPrice") int seckillPrice,
            @RequestParam("oldPrice") int oldPrice,
            Map<String, Object> resultMap) {
        try {
            SeckillActivity seckillActivity = new SeckillActivity();
            seckillActivity.setActivityName(activityName);
            seckillActivity.setGoodsId(goodsId);

            // Format start and end times
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            seckillActivity.setStartTime(format.parse(startTime.replace('T', ' ')));
            seckillActivity.setEndTime(format.parse(endTime.replace('T', ' ')));

            seckillActivity.setAvailableStock(availableStock);
            // Default status to "1" for on sale
            seckillActivity.setActivityStatus(1);
            // Initialize lock stock to "0"
            seckillActivity.setLockStock(0);
            seckillActivity.setSeckillPrice(seckillPrice);
            seckillActivity.setOldPrice(oldPrice);
            seckillActivity.setCreateTime(new Date());

            seckillActivityService.insertSeckillActivity(seckillActivity);
            resultMap.put("seckillActivity", seckillActivity);

            return "add_skill_activity";
        } catch (Exception e) {
            log.error("Error while adding seckill activity", e);
            return "500"; // Consider a more appropriate error handling view or action
        }
    }

    /**
     * 跳转到推送缓存预热页面
     *
     * @return
     */
    @RequestMapping("/pushSeckillCache")
    public String pushSeckillCache() {
        return "push_seckill_cache";
    }

    /**
     * 将对应的秒杀活动信息写入缓存中
     *
     * @param seckillId
     * @return
     */
    @RequestMapping("/pushSeckillCacheAction")
    public String pushSkilCache(@RequestParam("seckillId") long seckillId) {
        //将秒杀库存写入缓存中
        seckillActivityService.pushSeckillActivityInfoToCache(seckillId);
        return "push_seckill_cache";
    }


}

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
     * 跳转到秒杀活动页面
     *
     * @return
     */
    @RequestMapping("/addSkillActivity")
    public String addSkillActivity() {
        return "add_skill_activity";
    }


    /**
     * 添加秒杀活动信息
     *
     * @param activityName
     * @param goodsId
     * @param startTime
     * @param endTime
     * @param availableStock
     * @param seckillPrice
     * @param oldPrice
     * @param resultMap
     * @return
     */
    @RequestMapping("/addSkillActivityAction")
    public String addSkillActivityAction(@RequestParam("activityName") String activityName,
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

            //获取到的startTime时间格式  2022-10-05T22:51
            startTime = startTime.substring(0, 10) + " " + startTime.substring(11);
            endTime = endTime.substring(0, 10) + " " + endTime.substring(11);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            seckillActivity.setStartTime(format.parse(startTime));
            seckillActivity.setEndTime(format.parse(endTime));
            seckillActivity.setAvailableStock(availableStock);
            //默认上架
            seckillActivity.setActivityStatus(1);
            //初始为0
            seckillActivity.setLockStock(0);
            seckillActivity.setSeckillPrice(seckillPrice);
            seckillActivity.setOldPrice(oldPrice);
            seckillActivity.setCreateTime(new Date());
            seckillActivityService.insertSeckillActivity(seckillActivity);
            resultMap.put("seckillActivity", seckillActivity);
            return "add_skill_activity";
        } catch (Exception e) {
            log.error("addSkillActivityAction error", e);
            return "500";
        }
    }

}

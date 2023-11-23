package com.jason.trade.goods.controller;

import com.alibaba.fastjson.JSON;
import com.jason.trade.goods.db.model.Goods;
import com.jason.trade.goods.service.GoodsService;
import com.jason.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SearchService searchService;

    @RequestMapping("/goods/test")
    @ResponseBody
    public String test() {
        return "hello word java!";
    }
    /**
     * @param goods
     * @return
     */
    @PostMapping("/goods/insertGoods")
    @ResponseBody
    public boolean insertGoods(@RequestBody Goods goods) {
        log.info("insertGoods goods:{}", JSON.toJSON(goods));
        return goodsService.insertGoods(goods);
    }

    /**
     * 查询商品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/goods/queryGoodsById")
    @ResponseBody
    public Goods queryGoodsById(long id) {
        log.info("queryGoodsById id:{}", id);
        return goodsService.queryGoodsById(id);
    }


    /**
     * 根据关键词搜索
     *
     * @param keyword
     * @param from
     * @param size
     * @return
     */
    @GetMapping("/goods/searchGoodsList")
    @ResponseBody
    public List<Goods> searchGoodsList(String keyword, int from, int size) {
        log.info("searchGoodsList keyword:{} from:{} size:{}", keyword, from, size);
        return searchService.searchGoodsList(keyword, from, size);
    }


    /**
     * 锁定商品的库存
     *
     * @param id
     * @return
     */
    @GetMapping("/goods/lockStock")
    @ResponseBody
    public boolean lockStock(long id) {
        log.info("lockStock id:{}", id);
        return goodsService.lockStock(id);
    }

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    @GetMapping("/goods/deductStock")
    @ResponseBody
    public boolean deductStock(long id) {
        log.info("deductStock id:{}", id);
        return goodsService.deductStock(id);
    }

    /**
     * 锁定的库存回补
     *
     * @param id
     * @return
     */
    @GetMapping("/goods/revertStock")
    @ResponseBody
    public boolean revertStock(long id) {
        log.info("revertStock id:{}", id);
        return goodsService.revertStock(id);
    }
}


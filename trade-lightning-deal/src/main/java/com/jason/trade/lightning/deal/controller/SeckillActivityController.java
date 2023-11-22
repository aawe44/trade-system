package com.jason.trade.lightning.deal.controller;

import com.alibaba.fastjson.JSON;
import com.jason.trade.common.model.TradeResultDTO;
import com.jason.trade.lightning.deal.client.model.Order;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import com.jason.trade.lightning.deal.service.SeckillActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class SeckillActivityController {

    @Autowired
    private SeckillActivityService seckillActivityService;

    /**
     * 插入一个秒杀活动
     *
     * @param seckillActivity
     * @return
     */
    @GetMapping("/seckill/insertSeckillActivity")
    @ResponseBody
    public boolean insertSeckillActivity(SeckillActivity seckillActivity) {
        log.info("insertSeckillActivity seckillActivity:{}", JSON.toJSON(seckillActivity));
        return seckillActivityService.insertSeckillActivity(seckillActivity);
    }

    /**
     * 查询秒杀活动
     *
     * @param id
     * @return
     */
    @GetMapping("/seckill/querySeckillActivityById")
    @ResponseBody
    public SeckillActivity querySeckillActivityById(long id) {
        log.info("querySeckillActivityById id:{}", id);
        return seckillActivityService.querySeckillActivityById(id);
    }

    @GetMapping("/seckill/queryActivitysByStatus")
    @ResponseBody
    public List<SeckillActivity> queryActivitysByStatus(int status) {
        log.info("queryActivitysByStatus status:{}", status);
        return seckillActivityService.queryActivitysByStatus(status);
    }

    /**
     * 处理秒杀请求
     *
     * @param seckillActivityId
     * @return
     */
    @GetMapping("/seckill/seckillActivityId")
    @ResponseBody
    public boolean processSeckillReqBase(long seckillActivityId) {
        log.info("processSeckillReqBase seckillActivityId:{}", seckillActivityId);
        return seckillActivityService.processSeckillReqBase(seckillActivityId);
    }

    /**
     * 处理秒杀请求
     *
     * @param userId
     * @param seckillActivityId
     * @return
     */
    @GetMapping("/seckill/processSeckill")
    @ResponseBody
    public TradeResultDTO<Order> processSeckill(long userId, long seckillActivityId) {
        TradeResultDTO<Order> res = new TradeResultDTO<>();
        try {
            log.info("processSeckill userId:{} seckillActivityId:{}", userId, seckillActivityId);
            Order order = seckillActivityService.processSeckill(userId, seckillActivityId);
            res.setData(order);
            res.setCode(200);
        } catch (Exception ex) {
            res.setCode(500);
            res.setErrorMessage(ex.getMessage());
        }
        return res;

    }

    /**
     * 锁定商品的库存
     *
     * @param id
     * @return
     */
    @GetMapping("/seckill/lockStock")
    @ResponseBody
    public boolean lockStock(long id) {
        log.info("lockStock id:{}", id);
        return seckillActivityService.lockStock(id);
    }

    /**
     * 库存扣减
     *
     * @param id
     * @return
     */
    @GetMapping("/seckill/deductStock")
    @ResponseBody
    public boolean deductStock(long id) {
        log.info("deductStock id:{}", id);
        return seckillActivityService.deductStock(id);
    }

    /**
     * 锁定的库存回补
     *
     * @param id
     * @return
     */
    @GetMapping("/seckill/revertStock")
    @ResponseBody
    public boolean revertStock(long id) {
        log.info("revertStock id:{}", id);
        return seckillActivityService.revertStock(id);
    }

    /**
     * 缓存预热
     * 将秒杀信息写入Redis中
     *
     * @param id
     */
    @GetMapping("/seckill/pushSeckillActivityInfoToCache")
    @ResponseBody
    void pushSeckillActivityInfoToCache(long id) {
        log.info("pushSeckillActivityInfoToCache id:{}", id);
        seckillActivityService.pushSeckillActivityInfoToCache(id);
    }
}

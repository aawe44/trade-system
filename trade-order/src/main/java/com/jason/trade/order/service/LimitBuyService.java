package com.jason.trade.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
public class LimitBuyService {

    @Autowired
    private JedisPool jedisPool;

    private String SECKILL_ACTIVITY_MEMBERS = "seckill_activity_members";


    /**
     * 添加限制购买的名单
     * redis sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略
     *
     * @param seckillActivityId
     * @param userId
     */
    public void addLimitMember(long seckillActivityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd(SECKILL_ACTIVITY_MEMBERS + ":" + seckillActivityId, String.valueOf(userId));
        jedisClient.close();
        log.info("添加限制购买名单 userId:{}  seckillActivityId:{}  ", userId, seckillActivityId);
    }

    /**
     * 移除限制购买的名单
     * redis srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略
     *
     * @param seckillActivityId
     * @param userId
     */
    public void removeLimitMember(long seckillActivityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem(SECKILL_ACTIVITY_MEMBERS + ":" + seckillActivityId, String.valueOf(userId));
        jedisClient.close();
        log.info("移除限制购买名单 userId:{}  seckillActivityId:{}  ", userId, seckillActivityId);
    }

    /**
     * 判断是否在限制购卖名单中
     * redis sismember 命令判断成员元素是否是集合的成员
     *
     * @param seckillActivityId
     * @param userId
     * @return
     */
    public boolean isInLimitMember(long seckillActivityId, long userId) {
        Jedis jedisClient = jedisPool.getResource();
        boolean sismember = jedisClient.sismember(SECKILL_ACTIVITY_MEMBERS + ":" + seckillActivityId, String.valueOf(userId));
        jedisClient.close();
        log.info("是否在限购名单中:{}  userId:{}  seckillActivityId:{}  ", sismember, userId, seckillActivityId);
        return sismember;
    }


}

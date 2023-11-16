package com.jason.trade.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Service
public class RiskBlackListService {
    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加风控黑名单
     * redis sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略
     *
     * @param userId
     */
    public void addRiskBlackListMember(long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd("risk_black_list_members", String.valueOf(userId));
        jedisClient.close();
        log.info("添加风控黑名单 userId:{}", userId);
    }

    /**
     * 移除风控黑名单
     * redis srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略
     *
     * @param userId
     */
    public void removeRiskBlackListMember(long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem("risk_black_list_members", String.valueOf(userId));
        jedisClient.close();
        log.info("移除风控黑名单 userId:{}", userId);
    }

    /**
     * 判断是否风控黑名单中
     * redis sismember 命令判断成员元素是否是集合的成员
     *
     * @param userId
     * @return
     */
    public boolean isInRiskBlackListMember(long userId) {
        Jedis jedisClient = jedisPool.getResource();
        boolean sismember = jedisClient.sismember("risk_black_list_members", String.valueOf(userId));
        jedisClient.close();
        log.info("是否在风控黑名单:{}  userId:{}", sismember, userId);
        return sismember;
    }
}

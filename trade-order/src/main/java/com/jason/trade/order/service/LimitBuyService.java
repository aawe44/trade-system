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

    private static final String SECKILL_ACTIVITY_MEMBERS = "seckill_activity_members";

    /**
     * Adds a user to the limit buy list for a specific seckill activity.
     *
     * @param seckillActivityId
     * @param userId
     */
    public void addLimitMember(long seckillActivityId, long userId) {
        try {
            Jedis jedisClient = jedisPool.getResource();
            jedisClient.sadd(getKey(seckillActivityId), String.valueOf(userId));
            jedisClient.close();
            log.info("Added to the limit buy list. userId:{} seckillActivityId:{}", userId, seckillActivityId);
        } catch (Exception e) {
            log.error("Error adding to the limit buy list.", e);
        }
    }

    /**
     * Removes a user from the limit buy list for a specific seckill activity.
     *
     * @param seckillActivityId
     * @param userId
     */
    public void removeLimitMember(long seckillActivityId, long userId) {
        try {
            Jedis jedisClient = jedisPool.getResource();
            jedisClient.srem(getKey(seckillActivityId), String.valueOf(userId));
            jedisClient.close();
            log.info("Removed from the limit buy list. userId:{} seckillActivityId:{}", userId, seckillActivityId);
        } catch (Exception e) {
            log.error("Error removing from the limit buy list.", e);
        }
    }

    /**
     * Checks if a user is in the limit buy list for a specific seckill activity.
     *
     * @param seckillActivityId
     * @param userId
     * @return true if the user is in the limit buy list, false otherwise
     */
    public boolean isInLimitMember(long seckillActivityId, long userId) {
        try {
            Jedis jedisClient = jedisPool.getResource();
            boolean isInLimit = jedisClient.sismember(getKey(seckillActivityId), String.valueOf(userId));
            jedisClient.close();
            log.info("Is in the limit buy list:{} userId:{} seckillActivityId:{}", isInLimit, userId, seckillActivityId);
            return isInLimit;
        } catch (Exception e) {
            log.error("Error checking if the user is in the limit buy list.", e);
            return false;
        }
    }

    /**
     * Generates the key for the seckill activity in the limit buy list.
     *
     * @param seckillActivityId
     * @return the key for the seckill activity in the limit buy list
     */
    private String getKey(long seckillActivityId) {
        return SECKILL_ACTIVITY_MEMBERS + ":" + seckillActivityId;
    }
}

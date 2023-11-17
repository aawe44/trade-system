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
     * Adds a user to the risk black list.
     *
     * @param userId The ID of the user to be added to the risk black list.
     */
    public void addRiskBlackListMember(long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.sadd("risk_black_list_members", String.valueOf(userId));
        jedisClient.close();
        log.info("Added user to the risk black list. userId: {}", userId);
    }

    /**
     * Removes a user from the risk black list.
     *
     * @param userId The ID of the user to be removed from the risk black list.
     */
    public void removeRiskBlackListMember(long userId) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.srem("risk_black_list_members", String.valueOf(userId));
        jedisClient.close();
        log.info("Removed user from the risk black list. userId: {}", userId);
    }

    /**
     * Checks if a user is in the risk black list.
     *
     * @param userId The ID of the user to be checked.
     * @return True if the user is in the risk black list, false otherwise.
     */
    public boolean isInRiskBlackListMember(long userId) {
        Jedis jedisClient = jedisPool.getResource();
        boolean sismember = jedisClient.sismember("risk_black_list_members", String.valueOf(userId));
        jedisClient.close();
        log.info("User is{} in the risk black list. userId: {}", sismember ? "" : " not", userId);
        return sismember;
    }
}

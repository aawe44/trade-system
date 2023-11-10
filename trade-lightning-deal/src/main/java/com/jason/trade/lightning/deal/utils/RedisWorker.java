package com.jason.trade.lightning.deal.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

@Slf4j
@Service
public class RedisWorker {

    @Autowired
    private JedisPool jedisPool;

    /**
     * Set a key-value pair in Redis.
     *
     * @param key   The key to set.
     * @param value The value to set.
     */
    public void setValue(String key, String value) {
        try (Jedis jedisClient = jedisPool.getResource()) {
            jedisClient.set(key, value);
        }
    }

    /**
     * Get the value associated with a key from Redis.
     *
     * @param key The key to retrieve the value for.
     * @return The value associated with the key.
     */
    public String getValueByKey(String key) {
        Jedis jedisClient = jedisPool.getResource();
        String value = jedisClient.get(key);
        jedisClient.close();
        return value;
    }

    /**
     * Set a key-value pair in Redis with a long value.
     *
     * @param key   The key to set.
     * @param value The long value to set.
     */
    public void setValue(String key, Long value) {
        Jedis jedisClient = jedisPool.getResource();
        jedisClient.set(key, value.toString());
        jedisClient.close();
    }

    /**
     * Check and deduct stock using a Lua script in Redis.
     *
     * @param key The key representing the stock in Redis.
     * @return True if the stock deduction was successful, false otherwise.
     */
    public boolean stockDeductCheck(String key) {
        Jedis jedisClient = null;
        try {
            jedisClient = jedisPool.getResource();

            String script = "if redis.call('exists',KEYS[1]) == 1 then\n" +
                    "                 local availableStock = tonumber(redis.call('get', KEYS[1]))\n" +
                    "                 if( availableStock <=0 ) then\n" +
                    "                    return -1\n" +
                    "                 end;\n" +
                    "                 redis.call('decr',KEYS[1]);\n" +
                    "                 return availableStock - 1;\n" +
                    "             end;\n" +
                    "             return -1;";

            long scriptResult = (Long) jedisClient.eval(script,
                    Collections.singletonList(key),
                    Collections.emptyList());

            if (scriptResult < 0) {
                log.info("Unfortunately, the stock is insufficient, and the purchase failed");
                return false;
            } else {
                log.info("Purchase successful, congratulations!");
            }
            return true;
        } catch (Exception e) {
            log.error("Stock deduction exception: {}", e.getMessage());
            return false;
        } finally {
            if (jedisClient != null) {
                jedisClient.close();
            }
        }
    }
}

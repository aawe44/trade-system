package com.jason.trade.lightning.deal.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    // Redis server configuration
    private final String host = "localhost";
    private final int port = 6379;
    private final int timeout = 5000;

    // Connection pool settings
    private final int maxActive = 8;
    private final int maxIdle = 8;
    private final int minIdle = 0;
    private final long maxWaitMillis = -1;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = createJedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, null);

        logPoolConfiguration();
        return jedisPool;
    }

    private JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        return jedisPoolConfig;
    }

    private void logPoolConfiguration() {
        logger.info("JedisPool configuration successful!");
        logger.info("Redis server address: {}:{}", host, port);
    }
}

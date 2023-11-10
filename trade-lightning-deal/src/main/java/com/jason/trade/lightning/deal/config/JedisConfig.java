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
    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    //Redis服务器地址
    private final String host = "localhost";

    //Redis服务器连接端口
    private final int port = 6379;

    //连接超时时间（毫秒） 建立连接的最长的等待时间。使用负值无限期等待
    private final int timeout = 5000;

    //连接池最大连接数（使用负值表示没有限制）
    private final int maxActive = 8;

    //连接池中的最大空闲连接
    private final int maxIdle = 8;

    //连接池中的最小空闲连接
    private final int minIdle = 0;

    //连接池最大阻塞等待时间（使用负值表示没有限制）
    private final long maxWaitMillis = -1;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, null);

        logger.info("JedisPool注入成功！");
        logger.info("redis地址：" + host + ":" + port);
        return jedisPool;
    }
}

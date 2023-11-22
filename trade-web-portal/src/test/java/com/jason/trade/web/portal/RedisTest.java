package com.jason.trade.web.portal;


import com.jason.trade.common.utils.RedisWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisWorker redisWorker;

    @Test
    public void testSetValueInRedis() {
        // Test setting a key-value pair in Redis
        redisWorker.setValue("testName", "Hello");
    }

    @Test
    public void testGetValueFromRedis() {
        // Test getting the value associated with a key from Redis
        System.out.println(redisWorker.getValueByKey("testName"));
    }

    @Test
    public void testSetStockInRedis() {
        // Test setting stock for a seckill activity in Redis
        // Format: stock:{seckillActivityId} -> stock value
        redisWorker.setValue("stock:9", 1000L);
    }

    @Test
    public void testGetStockValueFromRedis() {
        // Test getting the stock value for a seckill activity from Redis
        System.out.println(redisWorker.getValueByKey("stock:9"));
    }

    @Test
    public void testStockDeductCheckInRedis() {
        // Test stock deduction check using Redis Lua script
        // Format: stock:{seckillActivityId} -> stock value
        redisWorker.stockDeductCheck("stock:668899");

        // Print the updated stock value after the deduction check
        System.out.println(redisWorker.getValueByKey("stock:668899"));
    }
}

package com.jason.trade.lightning.deal;

import com.jason.trade.goods.service.GoodsService;
import com.jason.trade.lightning.deal.db.dao.SeckillActivityDao;
import com.jason.trade.lightning.deal.db.model.SeckillActivity;
import com.jason.trade.lightning.deal.utils.RedisWorker;
import com.jason.trade.order.mq.OrderMessageSender;
import com.jason.trade.order.service.LimitBuyService;
import com.jason.trade.order.utils.SnowflakeIdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit tests for SeckillActivityService.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillActivityServiceTest {

    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Autowired
    private RedisWorker redisWorker;

    @Test
    public void pushSeckillActivityInfoToCacheTest() {
        // Test setup
        long seckillActivityId = 8L;

        // Use the updated variable name
        System.out.println(redisWorker.getValueByKey("stock:" + seckillActivityId));


        // Retrieve SeckillActivity by id
        SeckillActivity seckillActivity = seckillActivityDao.querySeckillActivityById(seckillActivityId);

        // Print stock information
        System.out.println(redisWorker.getValueByKey("stock:" + seckillActivityId));

        // Print complete activity information
        System.out.println(redisWorker.getValueByKey("seckillActivity:" + seckillActivity.getId()));

        // Print activity's associated goods information
        System.out.println(redisWorker.getValueByKey("seckillActivity_goods:" + seckillActivity.getGoodsId()));
    }
}

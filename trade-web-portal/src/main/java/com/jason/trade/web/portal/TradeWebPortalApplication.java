package com.jason.trade.web.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableRabbit
@ComponentScan(basePackages = {"com.jason"})
@MapperScan({"com.jason.trade.goods.db.mappers", "com.jason.trade.order.db.mappers", "com.jason.trade.lightning.deal.db.mappers"})
@SpringBootApplication
public class TradeWebPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeWebPortalApplication.class, args);
    }

}

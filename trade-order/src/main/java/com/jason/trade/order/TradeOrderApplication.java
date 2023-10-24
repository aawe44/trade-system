package com.jason.trade.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.jason"})
@MapperScan({"com.jason.trade.order.db.mappers","com.jason.trade.goods.db.mappers",})
@SpringBootApplication
public class TradeOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeOrderApplication.class, args);
	}

}

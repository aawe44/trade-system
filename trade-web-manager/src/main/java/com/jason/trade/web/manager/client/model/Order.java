package com.jason.trade.web.manager.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
public class Order {
    private Long id;
    private Long goodsId;
    private Integer payPrice;
    private Long userId;
    private Integer status;
    private Long activityId;
    private Integer activityType;
    private Date payTime;
    private Date createTime;
}
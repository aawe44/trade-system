package com.jason.trade.lightning.deal.client.model;

import lombok.*;

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
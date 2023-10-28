package com.jason.trade.order.db.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public void setPayPrice(Integer payPrice) {
        this.payPrice = payPrice;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
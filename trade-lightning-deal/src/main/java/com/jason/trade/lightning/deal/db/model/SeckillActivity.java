package com.jason.trade.lightning.deal.db.model;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillActivity {
    private Long id;
    private String activityName;
    private Long goodsId;
    private Date startTime;
    private Date endTime;
    private Integer availableStock;
    private Integer lockStock;
    private Integer activityStatus;
    private Integer seckillPrice;
    private Integer oldPrice;
    private Date createTime;
}

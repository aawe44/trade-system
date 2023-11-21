package com.jason.trade.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TradeResultDTO<T> implements Serializable {
    private int code;
    //错误提示信息
    private String errorMessage;
    //返回的数据
    private T data;
}

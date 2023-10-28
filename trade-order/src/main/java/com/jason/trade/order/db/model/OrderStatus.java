package com.jason.trade.order.db.model;

import lombok.Getter;


public enum OrderStatus {


    INVALID_ORDER(0, "INVALID_ORDER"),
    AWAITING_ORDER(1, "AWAITING_ORDER"),
    COMPLETED_ORDER(2, "COMPLETED_ORDER");


    @Getter
    int code;

    @Getter
    String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

}

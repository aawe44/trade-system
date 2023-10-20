package com.jason.trade.web.portal.util;

import java.math.BigDecimal;

public class CommonUtils {


    public static String changeF2Y(int price) {
        return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString();
    }

}
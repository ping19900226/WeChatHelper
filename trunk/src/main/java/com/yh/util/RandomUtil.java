package com.yh.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class RandomUtil {

    public static String getNumber(int length) {
        return String.valueOf(new BigDecimal(Math.random()).multiply(new BigDecimal(100000000000000000L)).longValue()).substring(0, length);
    }

    public static String getFixedNumber(int length) {
        double num = Math.random();
        NumberFormat ddf1=NumberFormat.getNumberInstance() ;

        ddf1.setMaximumFractionDigits(15);
        return ddf1.format(num) ;
    }
}

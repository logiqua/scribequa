package com.javax0.logiqua.commands.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class Equals {

    public static boolean equals(Object a, Object b){
        return switch(a){
            case BigDecimal bd -> bd.compareTo((BigDecimal)b) == 0;
            default -> Objects.equals(a,b);
        };
    }

}

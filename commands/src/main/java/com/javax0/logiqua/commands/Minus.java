package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.math.BigDecimal;
import java.math.BigInteger;

@Named.Symbol("-")
@Operation.Limited(min = 1)
public class Minus extends Operator {
    @Override
    Object unary(Object accumulator) {
        return switch (accumulator){
            case Byte b -> -b;
            case Short s -> -s;
            case Integer i -> -i;
            case Long l -> -l;
            case Float f -> -f;
            case Double d -> -d;
            case BigDecimal bd -> bd.negate();
            case BigInteger bi -> bi.negate();
            default ->
                    throw new IllegalArgumentException("Cannot subtract " + accumulator.getClass().getName());
        };

    }

    @Override
    Object binary(Object accumulator, Object arg) {
        return switch (accumulator) {
            case Byte b -> (byte) (b - toByte(arg));
            case Short s -> (short) (s - toShort(arg));
            case Integer i -> i - toInteger(arg);
            case Long l -> l - toLong(arg);
            case Float f -> f - toFloat(arg);
            case Double d -> d - toDouble(arg);
            case BigDecimal bd -> bd.subtract(toBigDecimal(arg));
            case BigInteger bi -> bi.subtract(toBigInteger(arg));
            default ->
                    throw new IllegalArgumentException("Cannot subtract " + accumulator.getClass().getName() + " to " + arg.getClass().getName());
        };
    }
}

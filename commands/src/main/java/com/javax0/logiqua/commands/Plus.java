package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.math.BigDecimal;
import java.math.BigInteger;

@Named.Symbol("+")
@Operation.Arity(min = 1)
public class Plus extends Operator {
    @Override
    Object unary(Object accumulator) {
        if (accumulator instanceof String s) {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                try {
                    return Double.parseDouble(s);
                }catch(NumberFormatException e2){
                    return s;
                }
            }
        }
        return accumulator;
    }

    @Override
    Object binary(Object accumulator, Object arg) {
        return switch (accumulator) {
            case Byte b -> (byte) (b + toByte(arg));
            case Short s -> (short) (s + toShort(arg));
            case Integer i -> i + toInteger(arg);
            case Long l -> l + toLong(arg);
            case Float f -> f + toFloat(arg);
            case Double d -> d + toDouble(arg);
            case BigDecimal bd -> bd.add(toBigDecimal(arg));
            case BigInteger bi -> bi.add(toBigInteger(arg));
            case String s -> s + arg;
            default ->
                    throw new IllegalArgumentException("Cannot add " + accumulator.getClass().getName() + " to " + arg.getClass().getName());
        };
    }
}

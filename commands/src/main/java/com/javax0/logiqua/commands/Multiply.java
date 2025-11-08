package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.math.BigDecimal;
import java.math.BigInteger;

@Named.Symbol("*")
@Operation.Limited(min = 2)
public class Multiply extends Operator {
    @Override
    Object unary(Object accumulator) {
        throw new IllegalArgumentException("Cannot multiply a single number " + accumulator.getClass().getName());
    }

    @Override
    Object binary(Object accumulator, Object arg) {
        return switch (accumulator) {
            case Byte b -> (byte) (b * toByte(arg));
            case Short s -> (short) (s * toShort(arg));
            case Integer i -> i * toInteger(arg);
            case Long l -> l * toLong(arg);
            case Float f -> f * toFloat(arg);
            case Double d -> d * toDouble(arg);
            case BigDecimal bd -> bd.multiply(toBigDecimal(arg));
            case BigInteger bi -> bi.parallelMultiply(toBigInteger(arg));
            default ->
                    throw new IllegalArgumentException("Cannot multiply " + accumulator.getClass().getName() + " to " + arg.getClass().getName());
        };
    }
}

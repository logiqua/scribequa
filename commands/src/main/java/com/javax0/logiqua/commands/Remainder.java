package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.math.BigDecimal;
import java.math.BigInteger;

@Named.Symbol("%")
@Operation.Arity(min = 2)
public class Remainder extends Operator {
    @Override
    protected Object unary(Object accumulator) {
        throw new IllegalArgumentException("Cannot multiply a single number " + accumulator.getClass().getName());
    }

    @Override
    protected Object binary(Object accumulator, Object arg) {
        return switch (accumulator) {
            case Byte b -> (byte) (b % (Byte) arg);
            case Short s -> (short) (s % (Short) arg);
            case Integer i -> i % (Integer) arg;
            case Long l -> l % (Long) arg;
            case Float f -> f % (Float) arg;
            case Double d -> d % (Double) arg;
            case BigDecimal bd -> bd.remainder((BigDecimal) arg);
            case BigInteger bi -> bi.remainder((BigInteger) arg);
            default ->
                    throw new IllegalArgumentException("Cannot calculate remainder " + accumulator.getClass().getName() + " to " + arg.getClass().getName());
        };
    }
}

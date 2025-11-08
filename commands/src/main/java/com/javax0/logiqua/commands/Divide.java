package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.math.BigDecimal;
import java.math.BigInteger;

@Named.Symbol("/")
@Operation.Limited(min = 2)
public class Divide extends Operator {
    @Override
    Object unary(Object accumulator) {
        throw new IllegalArgumentException("Cannot multiply a single number " + accumulator.getClass().getName());
    }

    @Override
    Object binary(Object accumulator, Object arg) {
        return switch (accumulator) {
            case Byte b -> {
                final var divisor = toByte(arg);
                if (divisor == 0) {
                    yield b > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                yield (byte) (b / divisor);
            }
            case Short s -> {
                final var divisor = toShort(arg);
                if (divisor == 0) {
                    yield s > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                yield (short) (s / divisor);
            }
            case Integer i -> {
                final var divisor = toInteger(arg);
                if (divisor == 0) {
                    yield i > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                yield i / divisor;
            }
            case Long l -> {
                final var divisor = toLong(arg);
                if (divisor == 0) {
                    yield l > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                yield l / divisor;
            }
            case Float f -> f / toFloat(arg);
            case Double d -> d / toDouble(arg);
            case BigDecimal bd -> bd.divide(toBigDecimal(arg));
            case BigInteger bi -> bi.divide(toBigInteger(arg));
            default ->
                    throw new IllegalArgumentException("Cannot divide " + accumulator.getClass().getName() + " to " + arg.getClass().getName());
        };
    }
}

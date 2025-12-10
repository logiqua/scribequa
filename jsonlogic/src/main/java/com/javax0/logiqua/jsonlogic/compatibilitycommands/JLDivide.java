package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.Operator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static com.javax0.logiqua.commands.Operator.*;

@Named.Symbol("/")
@Operation.Arity(min = 2)
public class JLDivide extends Operator {

    @Override
    protected Object unary(Object accumulator) {
        throw new IllegalArgumentException("Cannot multiply a single number " + accumulator.getClass().getName());
    }

    protected Object binary(Object accumulator, Object arg) {
        return switch (accumulator) {
            case Byte b -> {
                final var divisor = toByte(arg);
                if (divisor == 0) {
                    yield b > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                if( b % divisor == 0) {
                    yield (byte)b / divisor;
                }else {
                    yield ((double)b) / divisor;
                }
            }
            case Short s -> {
                final var divisor = toShort(arg);
                if (divisor == 0) {
                    yield s > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                if( s % divisor == 0) {
                    yield s / divisor;
                }else {
                    yield ((double)s) / divisor;
                }
            }
            case Integer i -> {
                final var divisor = toInteger(arg);
                if (divisor == 0) {
                    yield i > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                if( i % divisor == 0) {
                    yield i / divisor;
                }else {
                    yield ((double)i) / divisor;
                }
            }
            case Long l -> {
                final var divisor = toLong(arg);
                if (divisor == 0) {
                    yield l > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                }
                if( l % divisor == 0) {
                    yield l / divisor;
                } else {
                    yield ((double)l) / divisor;
                }
            }
            case Float f -> f / toFloat(arg);
            case Double d -> d / toDouble(arg);
            case BigDecimal bd -> {
                final var divisor = toBigDecimal(arg);
                if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                    yield bd.compareTo(BigDecimal.ZERO) > 0
                            ? Double.POSITIVE_INFINITY
                            : Double.NEGATIVE_INFINITY;
                }
                yield bd.divide(divisor, RoundingMode.HALF_DOWN);
            }
            case BigInteger bi -> {
                final var divisor = toBigInteger(arg);
                if (divisor.equals(BigInteger.ZERO)) {
                    yield bi.compareTo(BigInteger.ZERO) > 0
                            ? Double.POSITIVE_INFINITY
                            : Double.NEGATIVE_INFINITY;
                }
                yield bi.divide(divisor);
            }
            default ->
                    throw new IllegalArgumentException("Cannot divide " + accumulator.getClass().getName() + " to " + arg.getClass().getName());
        };
    }
}

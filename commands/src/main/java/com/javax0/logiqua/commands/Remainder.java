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
        throw new IllegalArgumentException("Cannot calculate the remainder of a single number " + accumulator.getClass().getName());
    }

    /**
     * Calculates the remainder of the division of two numbers (accumulator and arg).
     * The method supports various numeric types including Byte, Short, Integer, Long, Float,
     * Double, BigDecimal, and BigInteger.
     * <p>
     * Since this method is called from the abstract parent class where type promotion is handled,
     * the arg object is guaranteed to be of the same type as the accumulator. Therefore, any
     * ClassCastException would indicate an internal error rather than invalid input.
     *
     * @param accumulator the dividend in the remainder operation. This is the initial value.
     * @param arg         the divisor in the remainder operation. This value determines the remainder.
     * @return the result of the remainder operation. This may be a number or Double.NaN in cases of division by zero.
     * @throws IllegalArgumentException if the types of accumulator and arg are incompatible or unsupported for the operation.
     * @throws IllegalArgumentException if a division by zero occurs for types like BigInteger or BigDecimal.
     * @throws ClassCastException       if the input arguments cannot be cast to compatible numeric types.
     */
    @Override
    protected Object binary(Object accumulator, Object arg) {
        try {
            return switch (accumulator) {
                case Byte b -> (byte) (b % (Byte) arg);
                case Short s -> (short) (s % (Short) arg);
                case Integer i -> i % (Integer) arg;
                case Long l -> l % (Long) arg;
                case Float f -> f % (Float) arg;
                case Double d -> d % (Double) arg;
                case BigDecimal bd -> {
                    final var divisor = (BigDecimal) arg;
                    if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                        yield Double.NaN; // Consistent with floating-point behavior
                    }
                    yield bd.remainder(divisor);
                }
                case BigInteger bi -> {
                    final var divisor = (BigInteger) arg;
                    if (divisor.equals(BigInteger.ZERO)) {
                        yield Double.NaN;
                    }
                    yield bi.remainder(divisor);
                }
                default ->
                        throw new IllegalArgumentException("Cannot calculate remainder " + accumulator.getClass().getName() + " to " + arg.getClass().getName());
            };
        } catch (ClassCastException cce) {
            throw new IllegalArgumentException("Internal error calculating " + accumulator.getClass().getName() + " % " + arg.getClass().getName(), cce);
        }
    }
}

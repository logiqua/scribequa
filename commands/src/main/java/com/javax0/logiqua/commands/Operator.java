package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class Operator implements Operation.Function {
    abstract protected Object unary(Object accumulator);

    abstract protected Object binary(Object accumulator, Object arg);

    /**
     * Type promotion order: Byte < Short < Integer < Long < Float < Double < BigInteger < BigDecimal
     * When mixing integer and floating-point types, the result is the wider floating-point type.
     * When mixing BigInteger/BigDecimal with floating-point, result is BigDecimal.
     */
    private enum NumericType {
        BYTE(0),
        SHORT(1),
        INTEGER(2),
        LONG(3),
        FLOAT(4),
        DOUBLE(5),
        BIG_INTEGER(6),
        BIG_DECIMAL(7);

        private final int order;

        NumericType(int order) {
            this.order = order;
        }

        static NumericType of(Object value) {
            return switch (value) {
                case Byte ignored -> BYTE;
                case Short ignored -> SHORT;
                case Integer ignored -> INTEGER;
                case Long ignored -> LONG;
                case Float ignored -> FLOAT;
                case Double ignored -> DOUBLE;
                case BigInteger ignored -> BIG_INTEGER;
                case BigDecimal ignored -> BIG_DECIMAL;
                default -> null;
            };
        }

        /**
         * Determines the common type for two numeric values.
         * Rules:
         * - If both are integer types, use the wider integer type
         * - If one is floating-point, use the wider floating-point type
         * - BigInteger/BigDecimal with floating-point -> BigDecimal
         */
        static NumericType promote(NumericType left, NumericType right) {
            if (left == null || right == null) return null;

            // If both are integer types (Byte through Long)
            if (left.order <= LONG.order && right.order <= LONG.order) {
                return values()[Math.max(left.order, right.order)];
            }

            // If one is floating-point (Float or Double)
            if (left == FLOAT || left == DOUBLE || right == FLOAT || right == DOUBLE) {
                // If either is BigDecimal, result is BigDecimal
                if (left == BIG_DECIMAL || right == BIG_DECIMAL) {
                    return BIG_DECIMAL;
                }
                // If either is BigInteger, result is BigDecimal
                if (left == BIG_INTEGER || right == BIG_INTEGER) {
                    return BIG_DECIMAL;
                }
                // Otherwise, use the wider floating-point type
                return values()[Math.max(left.order, right.order)];
            }

            // Both are BigInteger or BigDecimal
            return values()[Math.max(left.order, right.order)];
        }

        Object convert(Object value) {
            return switch (this) {
                case BYTE -> toByte(value);
                case SHORT -> toShort(value);
                case INTEGER -> toInteger(value);
                case LONG -> toLong(value);
                case FLOAT -> toFloat(value);
                case DOUBLE -> toDouble(value);
                case BIG_INTEGER -> toBigInteger(value);
                case BIG_DECIMAL -> toBigDecimal(value);
            };
        }
    }

    // Conversion methods - using switch expressions for actual conversion
    public static Double toDouble(Object arg) {
        return switch (arg) {
            case Double d -> d;
            case Number n -> n.doubleValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to double");
        };
    }

    public static Byte toByte(Object arg) {
        return switch (arg) {
            case Byte b -> b;
            case Number n -> n.byteValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to byte");
        };
    }

    public static Short toShort(Object arg) {
        return switch (arg) {
            case Short s -> s;
            case Number n -> n.shortValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to short");
        };
    }

    public static Integer toInteger(Object arg) {
        return switch (arg) {
            case Integer i -> i;
            case Number n -> n.intValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to integer");
        };
    }

    public static Long toLong(Object arg) {
        return switch (arg) {
            case Long l -> l;
            case Number n -> n.longValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to long");
        };
    }

    public static Float toFloat(Object arg) {
        return switch (arg) {
            case Float f -> f;
            case Number n -> n.floatValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to float");
        };
    }

    public static BigDecimal toBigDecimal(Object arg) {
        return switch (arg) {
            case BigDecimal bd -> bd;
            case BigInteger bi -> new BigDecimal(bi);
            case Long l -> BigDecimal.valueOf(l);// different from 'n.doubleValue()' double when there are more than 53 bits
            case Number n -> BigDecimal.valueOf(n.doubleValue());
            default ->
                    throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to BigDecimal");
        };
    }

    public static BigInteger toBigInteger(Object arg) {
        return switch (arg) {
            case BigInteger bi -> bi;
            case BigDecimal bd -> bd.toBigInteger();
            case Number n -> BigInteger.valueOf(n.longValue());
            default ->
                    throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to BigInteger");
        };
    }

    private static Object convertNumeric(Object value, NumericType targetType) {
        if (value == null) return null;
        NumericType sourceType = NumericType.of(value);
        if (sourceType == null) {
            throw new IllegalArgumentException("Cannot convert " + value.getClass().getName() + " to numeric type");
        }
        if (sourceType == targetType) {
            return value;
        }
        return targetType.convert(value);
    }

    @Override
    public Object evaluate(Executor executor, Object... args) {
        var accumulator = args[0];
        if (accumulator instanceof String string) {
            final var castResult = executor.getContext().caster(String.class, Number.class)
                    .map(c -> c.cast(string));
            if (castResult.isPresent()) {
                accumulator = castResult.get();
            }
        }
        if (args.length == 1) {
            return unary(accumulator);
        }

        var accumulatorType = NumericType.of(accumulator);
        for (int i = 1; i < args.length; i++) {
            final var arg = args[i];
            final Object convertedArg;
            final var argType = NumericType.of(arg);
            if (accumulatorType != null && argType != null) {
                final var commonType = NumericType.promote(accumulatorType, argType);
                if (commonType != null) {
                    accumulator = convertNumeric(accumulator, commonType);
                    accumulatorType = commonType;
                    convertedArg = convertNumeric(arg, commonType);
                } else {
                    throw new IllegalArgumentException("Cannot convert " + arg + " to a numeric type");
                }
            } else {
                convertedArg = arg;
            }
            accumulator = binary(accumulator, convertedArg);
        }
        return accumulator;
    }
}

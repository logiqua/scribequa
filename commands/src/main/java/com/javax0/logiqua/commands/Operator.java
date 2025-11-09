package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class Operator implements Operation.Function {
    abstract Object unary(Object accumulator);

    abstract Object binary(Object accumulator, Object arg);


    public static Double toDouble(Object arg) {
        return switch (arg) {
            case Double d -> d;
            case Integer i -> i.doubleValue();
            case Long l -> l.doubleValue();
            case Short s -> s.doubleValue();
            case Byte b -> b.doubleValue();
            case Float f -> f.doubleValue();
            case BigInteger bi -> bi.doubleValue();
            case BigDecimal bd -> bd.doubleValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to double");
        };
    }

    public static Byte toByte(Object arg) {
        return switch (arg) {
            case Byte b -> b;
            case Short s -> s.byteValue();
            case Integer i -> i.byteValue();
            case Long l -> l.byteValue();
            case Float f -> f.byteValue();
            case Double d -> d.byteValue();
            case BigInteger bi -> bi.byteValue();
            case BigDecimal bd -> bd.byteValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to byte");
        };
    }

    public static Short toShort(Object arg) {
        return switch (arg) {
            case Short s -> s;
            case Byte b -> b.shortValue();
            case Integer i -> i.shortValue();
            case Long l -> l.shortValue();
            case Float f -> f.shortValue();
            case Double d -> d.shortValue();
            case BigInteger bi -> bi.shortValue();
            case BigDecimal bd -> bd.shortValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to short");
        };
    }

    public static Integer toInteger(Object arg) {
        return switch (arg) {
            case Integer i -> i;
            case Byte b -> b.intValue();
            case Short s -> s.intValue();
            case Long l -> l.intValue();
            case Float f -> f.intValue();
            case Double d -> d.intValue();
            case BigInteger bi -> bi.intValue();
            case BigDecimal bd -> bd.intValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to integer");
        };
    }

    public static Long toLong(Object arg) {
        return switch (arg) {
            case Long l -> l;
            case Byte b -> b.longValue();
            case Short s -> s.longValue();
            case Integer i -> i.longValue();
            case Float f -> f.longValue();
            case Double d -> d.longValue();
            case BigInteger bi -> bi.longValue();
            case BigDecimal bd -> bd.longValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to long");
        };
    }

    public static Float toFloat(Object arg) {
        return switch (arg) {
            case Float f -> f;
            case Byte b -> b.floatValue();
            case Short s -> s.floatValue();
            case Integer i -> i.floatValue();
            case Long l -> l.floatValue();
            case Double d -> d.floatValue();
            case BigInteger bi -> bi.floatValue();
            case BigDecimal bd -> bd.floatValue();
            default -> throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to float");
        };
    }

    public static BigDecimal toBigDecimal(Object arg) {
        return switch (arg) {
            case Byte b -> BigDecimal.valueOf(b);
            case Short s -> BigDecimal.valueOf(s);
            case Integer i -> BigDecimal.valueOf(i);
            case Long l -> BigDecimal.valueOf(l);
            case Float f -> BigDecimal.valueOf(f);
            case Double d -> BigDecimal.valueOf(d);
            case BigInteger bi -> new BigDecimal(bi);
            case BigDecimal bd -> bd;
            default ->
                    throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to BigDecimal");
        };
    }

    public static BigInteger toBigInteger(Object arg) {
        return switch (arg) {
            case Byte b -> BigInteger.valueOf(b);
            case Short s -> BigInteger.valueOf(s);
            case Integer i -> BigInteger.valueOf(i);
            case Long l -> BigInteger.valueOf(l);
            case Float f -> BigInteger.valueOf(f.longValue());
            case Double d -> BigInteger.valueOf(d.longValue());
            case BigInteger bi -> bi;
            case BigDecimal bd -> bd.toBigInteger();
            default ->
                    throw new IllegalArgumentException("Cannot convert " + arg.getClass().getName() + " to BigInteger");
        };
    }


    @Override
    public Object evaluate(Executor executor, Object... args) {
        var accumulator = args[0];
        if (accumulator instanceof String string) {
            final var op = executor.getContext().caster(String.class, Number.class)
                    .map(c -> c.cast(string));
            if (op.isPresent()) {
                accumulator = op.get();
            }
        }
        if (args.length == 1) {
            return unary(accumulator);
        }
        for (int i = 1; i < args.length; i++) {
            final var acc = accumulator;
            final var arg = args[i];
            accumulator = binary(accumulator, executor.getContext().caster(Context.classOf(arg), Context.classOf(accumulator))
                    .map(c -> c.cast(arg)).orElseGet(() ->
                            switch (acc) {
                                case Byte ignore -> toByte(arg);
                                case Short ignore -> toShort(arg);
                                case Integer ignore -> toInteger(arg);
                                case Long ignore -> toLong(arg);
                                case Float ignore -> toFloat(arg);
                                case Double ignore -> toDouble(arg);
                                case BigDecimal ignore -> toBigDecimal(arg);
                                case BigInteger ignore -> toBigInteger(arg);
                                default ->
                                        throw new IllegalArgumentException("Cannot calculate " + this.getClass().getSimpleName().toLowerCase() + " " + acc.getClass().getName() + " to " + arg.getClass().getName());
                            }));
        }
        return accumulator;
    }
}

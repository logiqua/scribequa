package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import static com.javax0.logiqua.commands.Operator.*;

public class TestNumericOperators {

    private static final Map<String, Object> context = Map.of("aByte", (byte) 1,
            "aShort", (short) 1,
            "aInt", 1,
            "aLong", 1L,
            "aFloat", (float) 1.0,
            "aDouble", 1.0,
            "aBigInteger", BigInteger.ONE,
            "aBigDecimal", BigDecimal.ONE
    );

    private void test(final Object expected, final String script) {
        final var scriptObject = new ExpLogiqua().with(context).compile(script);
        final var result = scriptObject.evaluate();
        switch (result) {
            case Byte b -> Assertions.assertEquals(toByte(expected), b);
            case Short s -> Assertions.assertEquals(toShort(expected), s);
            case Integer i -> Assertions.assertEquals(toInteger(expected), i);
            case Long l -> Assertions.assertEquals(toLong(expected), l);
            case Float f -> Assertions.assertEquals(toFloat(expected), f);
            case Double d -> Assertions.assertEquals(toDouble(expected), d);
            case BigInteger bi -> Assertions.assertEquals(toBigInteger(expected), bi);
            case BigDecimal bd -> Assertions.assertEquals(0, toBigDecimal(expected).compareTo(bd));
            default -> Assertions.fail("Unexpected type of the result");
        }
    }

    @Test
    void simpleAddition(){
        test(3L, "1 +2");
    }

    @Test
    void testOperations() {
        for (final var operator : new String[]{"+", "-", "*", "/", "%"}) {
            for (final var op1 : context.keySet()) {
                for (final var op2 : context.keySet()) {

                    final var script = String.format("""
                            var("%s") %s var("%s")
                            """,  op1, operator, op2);
                    test(operator.equals("+") ? 2 :
                                    operator.equals("-") || operator.equals("%") ? 0 : 1,
                            script);
                }
            }
        }
    }

}

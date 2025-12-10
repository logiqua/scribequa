package com.javax0.logiqua.exp;

import com.javax0.logiqua.Script;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class TestNumericConversion {

    private static final Map<String, Object> context = Map.of(
            "aByte", (byte) 42,
            "aShort", (short) 100,
            "aInt", 1000,
            "aLong", 10000L,
            "aFloat", 3.14f,
            "aDouble", 2.718,
            "aBigInteger", BigInteger.valueOf(999999),
            "aBigDecimal", BigDecimal.valueOf(123.456)
    );

    // Helper method to create a new ExpLogiqua instance with context for each compile
    private Script compileWithContext(String script) {
        return new ExpLogiqua().with(context).compile(script);
    }

    // Byte conversion tests
    @Test
    void testBytePlusByte() {
        final var result = compileWithContext("aByte + aByte").evaluate();
        Assertions.assertEquals(Byte.class, result.getClass());
        Assertions.assertEquals((byte) 84, result);
    }

    @Test
    void testBytePlusShort() {
        final var result = compileWithContext("aByte + aShort").evaluate();
        Assertions.assertEquals(Short.class, result.getClass());
        Assertions.assertEquals((short) 142, result);
    }

    @Test
    void testBytePlusInteger() {
        final var result = compileWithContext("aByte + aInt").evaluate();
        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(1042, result);
    }

    @Test
    void testBytePlusLong() {
        final var result = compileWithContext("aByte + aLong").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(10042L, result);
    }

    @Test
    void testBytePlusFloat() {
        final var result = compileWithContext("aByte + aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
        Assertions.assertEquals(45.14f, (Float) result, 0.001f);
    }

    @Test
    void testBytePlusDouble() {
        final var result = compileWithContext("aByte + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
        Assertions.assertEquals(44.718, (Double) result, 0.001);
    }

    // Short conversion tests
    @Test
    void testShortPlusShort() {
        final var result = compileWithContext("aShort + aShort").evaluate();
        Assertions.assertEquals(Short.class, result.getClass());
        Assertions.assertEquals((short) 200, result);
    }

    @Test
    void testShortPlusInteger() {
        final var result = compileWithContext("aShort + aInt").evaluate();
        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(1100, result);
    }

    @Test
    void testShortPlusLong() {
        final var result = compileWithContext("aShort + aLong").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(10100L, result);
    }

    @Test
    void testShortPlusFloat() {
        final var result = compileWithContext("aShort + aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
        Assertions.assertEquals(103.14f, (Float) result, 0.001f);
    }

    @Test
    void testShortPlusDouble() {
        final var result = compileWithContext("aShort + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
        Assertions.assertEquals(102.718, (Double) result, 0.001);
    }

    // Integer conversion tests
    @Test
    void testIntegerPlusInteger() {
        final var result = compileWithContext("aInt + aInt").evaluate();
        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(2000, result);
    }

    @Test
    void testIntegerPlusLong() {
        final var result = compileWithContext("aInt + aLong").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(11000L, result);
    }

    @Test
    void testIntegerPlusFloat() {
        final var result = compileWithContext("aInt + aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
        Assertions.assertEquals(1003.14f, (Float) result, 0.001f);
    }

    @Test
    void testIntegerPlusDouble() {
        final var result = compileWithContext("aInt + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
        Assertions.assertEquals(1002.718, (Double) result, 0.001);
    }

    // Long conversion tests
    @Test
    void testLongPlusLong() {
        final var result = compileWithContext("aLong + aLong").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(20000L, result);
    }

    @Test
    void testLongPlusFloat() {
        final var result = compileWithContext("aLong + aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
        Assertions.assertEquals(10003.14f, (Float) result, 0.001f);
    }

    @Test
    void testLongPlusDouble() {
        final var result = compileWithContext("aLong + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
        Assertions.assertEquals(10002.718, (Double) result, 0.001);
    }

    // Float conversion tests
    @Test
    void testFloatPlusFloat() {
        final var result = compileWithContext("aFloat + aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
        Assertions.assertEquals(6.28f, (Float) result, 0.001f);
    }

    @Test
    void testFloatPlusDouble() {
        final var result = compileWithContext("aFloat + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
        Assertions.assertEquals(5.858, (Double) result, 0.001);
    }

    // Double conversion tests
    @Test
    void testDoublePlusDouble() {
        final var result = compileWithContext("aDouble + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
        Assertions.assertEquals(5.436, (Double) result, 0.001);
    }

    // BigInteger conversion tests
    @Test
    void testBigIntegerPlusBigInteger() {
        final var result = compileWithContext("aBigInteger + aBigInteger").evaluate();
        Assertions.assertEquals(BigInteger.class, result.getClass());
        Assertions.assertEquals(BigInteger.valueOf(1999998), result);
    }

    @Test
    void testBigIntegerPlusBigDecimal() {
        final var result = compileWithContext("aBigInteger + aBigDecimal").evaluate();
        Assertions.assertEquals(BigDecimal.class, result.getClass());
        Assertions.assertEquals(0, new BigDecimal("1000122.456").compareTo((BigDecimal) result));
    }

    @Test
    void testBigIntegerPlusFloat() {
        final var result = compileWithContext("aBigInteger + aFloat").evaluate();
        Assertions.assertEquals(BigDecimal.class, result.getClass());
    }

    @Test
    void testBigIntegerPlusDouble() {
        final var result = compileWithContext("aBigInteger + aDouble").evaluate();
        Assertions.assertEquals(BigDecimal.class, result.getClass());
    }

    // BigDecimal conversion tests
    @Test
    void testBigDecimalPlusBigDecimal() {
        final var result = compileWithContext("aBigDecimal + aBigDecimal").evaluate();
        Assertions.assertEquals(BigDecimal.class, result.getClass());
        Assertions.assertEquals(0, new BigDecimal("246.912").compareTo((BigDecimal) result));
    }

    @Test
    void testBigDecimalPlusFloat() {
        final var result = compileWithContext("aBigDecimal + aFloat").evaluate();
        Assertions.assertEquals(BigDecimal.class, result.getClass());
    }

    @Test
    void testBigDecimalPlusDouble() {
        final var result = compileWithContext("aBigDecimal + aDouble").evaluate();
        Assertions.assertEquals(BigDecimal.class, result.getClass());
    }

    // Type promotion in arithmetic operations
    @Test
    void testBytePlusIntegerPlusLong() {
        final var result = compileWithContext("aByte + aInt + aLong").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(11042L, result);
    }

    @Test
    void testIntegerPlusFloatPromotion() {
        final var result = compileWithContext("aInt + aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
    }

    @Test
    void testLongPlusDoublePromotion() {
        final var result = compileWithContext("aLong + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
    }

    @Test
    void testFloatPlusDoublePromotion() {
        final var result = compileWithContext("aFloat + aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
    }

    // Multiplication with type promotion
    @Test
    void testByteTimesShort() {
        final var result = compileWithContext("aByte * aShort").evaluate();
        Assertions.assertEquals(Short.class, result.getClass());
        Assertions.assertEquals((short) 4200, result);
    }

    @Test
    void testShortTimesInteger() {
        final var result = compileWithContext("aShort * aInt").evaluate();
        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(100000, result);
    }

    @Test
    void testIntegerTimesLong() {
        final var result = compileWithContext("aInt * aLong").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(10000000L, result);
    }

    @Test
    void testLongTimesFloat() {
        final var result = compileWithContext("aLong * aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
    }

    @Test
    void testFloatTimesDouble() {
        final var result = compileWithContext("aFloat * aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
    }

    // Division with type promotion
    @Test
    void testIntegerDividedByInteger() {
        final var result = compileWithContext("aInt / aInt").evaluate();
        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(1, result);
    }

    @Test
    void testIntegerDividedByFloat() {
        final var result = compileWithContext("aInt / aFloat").evaluate();
        Assertions.assertEquals(Float.class, result.getClass());
    }

    @Test
    void testLongDividedByDouble() {
        final var result = compileWithContext("aLong / aDouble").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
    }

    // Subtraction with type promotion
    @Test
    void testLongMinusInteger() {
        final var result = compileWithContext("aLong - aInt").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(9000L, result);
    }

    @Test
    void testDoubleMinusFloat() {
        final var result = compileWithContext("aDouble - aFloat").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
    }

    @Test
    void testBigDecimalMinusBigInteger() {
        final var result = compileWithContext("aBigDecimal - aBigInteger").evaluate();
        Assertions.assertEquals(BigDecimal.class, result.getClass());
    }

    // Modulo with type promotion
    @Test
    void testIntegerModuloInteger() {
        final var result = compileWithContext("aInt % 3").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(1L, result);
    }

    @Test
    void testLongModuloInteger() {
        final var result = compileWithContext("aLong % aInt").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(0L, result);
    }

    // Complex expressions
    @Test
    void testMultipleOperationsWithTypePromotion() {
        final var result = compileWithContext("aByte + aShort * aInt - aLong / aFloat").evaluate();
        Assertions.assertTrue(result instanceof Float || result instanceof Double);
    }

    @Test
    void testLiteralIntNumberConversions() {
        final var exp = new ExpLogiqua();
        final var result = exp.compile("100 + 200").evaluate();
        Assertions.assertEquals(Long.class, result.getClass());
        Assertions.assertEquals(300L, result);
    }

    @Test
    void testLiteralFloatNumberConversions() {
        final var exp = new ExpLogiqua();
        final var result = exp.compile("100 + 3.14").evaluate();
        Assertions.assertEquals(Double.class, result.getClass());
        Assertions.assertEquals(103.14, (Double) result, 0.001);
    }

    @Test
    void testAllNumericTypesInOneExpression() {
        final var result = compileWithContext("""
                aByte + aShort + aInt + aLong + 
                aFloat + aDouble + aBigInteger + aBigDecimal
                """).evaluate();

        Assertions.assertEquals(BigDecimal.class, result.getClass());
    }
}

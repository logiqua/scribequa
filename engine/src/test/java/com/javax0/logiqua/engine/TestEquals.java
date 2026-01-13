package com.javax0.logiqua.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

public class TestEquals {

    @Test
    void testEqualsBigDecimal() {
        final var executor = Engine.withData(Map.of());
        final var script = executor.getOp("==").args(new BigDecimal("100"), new BigDecimal("100.00"));
        Assertions.assertEquals(true, script.evaluate());
    }

    @Test
    void testEqualsStringsTrue() {
        final var executor = Engine.withData(Map.of());
        final var script = executor.getOp("==").args("Hello", "Hello");
        Assertions.assertEquals(true, script.evaluate());
    }

    @Test
    void testEqualsStringsFalse() {
        final var executor = Engine.withData(Map.of());
        final var script = executor.getOp("==").args("Hello", "Hella");
        Assertions.assertEquals(false, script.evaluate());
    }

    @Test
    void testEqualsCoercibleTypes() {
        final var executor = Engine.withData(Map.of());
        final var script = executor.getOp("==").args(23, 23L);
        Assertions.assertEquals(true, script.evaluate());
    }

}

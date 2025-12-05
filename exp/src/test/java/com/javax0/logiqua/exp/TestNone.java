package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNone {

    @Test
    void testNoneTrue() {
        final var script = "none([1, 3, 5, 7, 9, 11], current % 2 == 0 )";
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testNoneOperation() {
        final var script = "[1, 3, 5, 7, 9, 11] none (current % 2 == 0)";
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testNoneFalse() {
        final var script = "none([1, 3, 5, 8], var(\"\") %2 == 0 )";
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(false, result);
    }
}

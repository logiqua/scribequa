package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAll {

    @Test
    void testAllTrue() {
        final var script = """
                all( [2, 4, 6, 8],
                     var("") % 2 == 0)
                """;
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAllOperation() {
        final var script = """
                [2, 4, 6, 8] all (current % 2 == 0)
                """;
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAllFalse() {
        final var script = """
                all([2, 4, 6, 7], var("") % 2 ==  0)
                """;
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(false, result);
    }
}

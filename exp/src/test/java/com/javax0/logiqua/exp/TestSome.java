package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSome {

    @Test
    void testSomeTrue() {
        final var script = """
                some ([1, 2, 5, 7, 9, 11],
                      current % 2 == 0
                )
                """;
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testSomeFalse() {
        final var script = """
                some([1, 3, 5, 7], current %2 == 0)
                """;
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(false, result);
    }
}

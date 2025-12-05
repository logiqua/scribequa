package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestDataAccess {

    @Test
    void testSimpleExpression() {
        final var script = "foo * bar";
        final var scriptObject = new ExpLogiqua().with(Map.of("foo", 42, "bar", 44)).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(1848, result);
    }

    @Test
    void testArrayAndFieldAccessExpression() {
        final var script = "var(\"foo[1]\") * bar";
        final var scriptObject = new ExpLogiqua().with(
                Map.of("foo", List.of(1,2,3,4),
                        "map" , Map.of( "a", -1, "b",-2),
                        "bar", 44)
        ).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(88, result);
    }

    @Test
    void testFieldAccessExpression() {
        final var script = "var(\"map.a\") * bar";
        final var scriptObject = new ExpLogiqua().with(
                Map.of("foo", List.of(1,2,3,4),
                        "map" , Map.of( "a", -1, "b",-2),
                        "bar", 44)
        ).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(-44, result);
    }

}

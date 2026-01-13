package com.javax0.logiqua.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestListAsExpression {

    @Test
    void testConstantListAsCode() {
        final var script = """
                [ 1,2,3 ]
                """;
        final var scriptObject = new JsonLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(new ArrayList<>(List.of(1L, 2L, 3L)), result);
    }

    @Test
    void testVariable() {
        final var script = """
                {"var":"x"}
                """;
        final var scriptObject = new JsonLogiqua().with(Map.of("x", 2L)).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(2L, result);
    }

    @Test
    void testVariableNonExistence() {
        final var script = """
                {"var": "z"}
                """;
        final var scriptObject = new JsonLogiqua().with(Map.of("x", 2L)).compile(script);
        Assertions.assertThrows( Exception.class, () -> scriptObject.evaluate());
    }

    @Test
    void testVariableNonExistenceWithDefaultValue() {
        final var script = """
                {"var": ["z", null]}
                """;
        final var scriptObject = new JsonLogiqua().with(Map.of("x", 2L)).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertNull( result);
    }

    @Test
    void testListAsCodeWithVariable() {
        final var script = """
                [1, {"var":"x"} ,3]
                """;
        final var scriptObject = new JsonLogiqua().with(Map.of("x", 2L)).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(new ArrayList<>(List.of(1L, 2L, 3L)), result);
    }

    @Test
    void testMapping() {
        final var script = """
                {"map" : [{"var" : "desserts"},{"var" : "qty"}]}
                """;
        final var scriptObject = new JsonLogiqua().with(Map.of("desserts", List.of(Map.of("name", "apple", "qty", 1),
                Map.of("name", "brownie", "qty", 2), Map.of("name", "cupcake", "qty", 3)))).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(new ArrayList<>(List.of(1, 2, 3)), result);
    }

    @Test
    void testMappingWCurrent() {
        final var script = """
                {"map" : [{"var" : "desserts"},{"var" : "current.qty"}]}
                """;
        final var scriptObject = new JsonLogiqua().with(Map.of("desserts", List.of(Map.of("name", "apple", "qty", 1),
                Map.of("name", "brownie", "qty", 2), Map.of("name", "cupcake", "qty", 3)))).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(new ArrayList<>(List.of(1, 2, 3)), result);
    }
}

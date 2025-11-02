package com.javax0.logiqua.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestMissingSome {

    @Test
    @DisplayName("Test providing just enough arguments")
    void testMissingSomeJustEnough() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("missing_some").args(1, List.of("a", "b", "c"));
        final var result = script.evaluate();
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Test providing more than enough arguments")
    void testMissingMoreThanEnough() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!", "b", "k"));
        final var script = engine.getOp("missing_some").args(1, List.of("a", "b", "c"));
        final var result = script.evaluate();
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Test providing not enough arguments")
    void testMissingSomeNotEnough() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("missing_some").args(2, List.of("a", "b", "c"));
        final var result = script.evaluate();
        Assertions.assertEquals(List.of("b", "c"), result);
    }
}

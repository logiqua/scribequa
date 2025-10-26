package com.javax0.logiqua.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestIf {
    @Test
    void testIfTrueReturnsFirst() {
        final var engine = Engine.withData(Map.of());
        final var script =
                engine.getOp("if").args(true, "Very well", "Too bad");
        Assertions.assertEquals("Very well", script.evaluate(engine));
    }

    @Test
    void testIfFalseReturnsSecond() {
        final var engine = Engine.withData(Map.of());
        final var script =
                engine.getOp("if").args(false, "Too bad", "Very well");
        Assertions.assertEquals("Very well", script.evaluate(engine));
    }

    @Test
    @DisplayName("An 'if' command cannot be built if it has no arguments")
    void testIfNeedsArgument() {
        final var engine = Engine.withData(Map.of());
        Assertions.assertThrows(IllegalArgumentException.class, () -> engine.getOp("if").args());
    }

    @Test
    void testIfNeedsTwoArgumentsMinimum() {
        final var engine = Engine.withData(Map.of());
        Assertions.assertThrows(IllegalArgumentException.class, () -> engine.getOp("if")
                .args(true));
    }

    @Test
    @DisplayName("An 'if' command cannot be built if it has more than three arguments")
    void testIfDoesNotDevourMoreThanThreeArguments() {
        final var engine = Engine.withData(Map.of());
        Assertions.assertThrows(IllegalArgumentException.class, () -> engine.getOp("if")
                .args(true, 2, 3, 4));
    }

    @Test
    @DisplayName("An 'if' command cannot be built if the first argument is not boolean")
    void testIfNeedsBooleanFirstArgument() {
        final var engine = Engine.withData(Map.of());
        Assertions.assertThrows(IllegalArgumentException.class, () -> engine.getOp("if")
                .args(1, 2, 3));
    }

    @Test
    @DisplayName("An 'if' command can be built if the first argument is a boolean and comes from a variable that is not known during build time")
    void testVarInIf() {
        final var engine = Engine.withData(Map.of("a", true));
        final var script = engine.getOp("if").args(
                engine.getOp("var").args("a"), "Hello, World!", "Goodbye");
        Assertions.assertEquals("Hello, World!", script.evaluate(engine));
    }

    @Test
    @DisplayName("An if command can be built using variable as first arg, but fails if it is not boolean")
    void testVarInIfFailRuntime() {
        final var engine = Engine.withData(Map.of("a", "not boolean"));
        final var script = engine.getOp("if").args(
                engine.getOp("var").args("a"), "Hello, World!", "Goodbye");
        Assertions.assertThrows(IllegalArgumentException.class, () -> script.evaluate(engine));
    }
}

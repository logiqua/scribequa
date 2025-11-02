package com.javax0.logiqua.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestMissing {

    @Test
    void testMissing() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("missing").args("a", "b", "c");
        final var result = script.evaluate();
        Assertions.assertEquals(List.of("b", "c"), result);
    }

    @Test
    void testMissingWithArray() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("missing").args((Object) new String[]{"a", "b", "c"});
        final var result = script.evaluate();
        Assertions.assertEquals(List.of("b", "c"), result);
    }

    @Test
    void testMissingWithList() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("missing").args(List.of("a", "b", "c"));
        final var result = script.evaluate();
        Assertions.assertEquals(List.of("b", "c"), result);
    }

    @Test
    void testMissingWithSet() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("missing").args(Set.of("a", "b", "c"));
        final var result = script.evaluate();
        Assertions.assertInstanceOf(List.class, result);
        final var list = (List<?>) result;
        Assertions.assertTrue(list.contains("b"));
        Assertions.assertTrue(list.contains("c"));
        Assertions.assertFalse(list.contains("a"));
    }

    @Test
    void testMissingWithIterable() {
        class MyIterable implements Iterable<String> {
            @Override
            public java.util.Iterator<String> iterator() {
                return List.of("a", "b", "c").iterator();
            }
        }
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("missing").args(new MyIterable());
        final var result = script.evaluate();
        Assertions.assertInstanceOf(List.class, result);
        final var list = (List<?>) result;
        Assertions.assertTrue(list.contains("b"));
        Assertions.assertTrue(list.contains("c"));
        Assertions.assertFalse(list.contains("a"));
    }
}

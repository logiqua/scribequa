package com.javax0.logiqua.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestVar {

    public String testField = "Hello, World!";

    @Test
    void testVar() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!"));
        final var script = engine.getOp("var").args("a");
        Assertions.assertEquals("Hello, World!", script.evaluate(engine));
    }

    @Test
    void testVarKey() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!", "b", Map.of("german", "Wilcommen, Welt")));
        final var script = engine.getOp("var").args("b.german");
        Assertions.assertEquals("Wilcommen, Welt", script.evaluate(engine));
    }

    @Test
    void testVarIndex() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!", "b", List.of("german", "Wilcommen, Welt")));
        final var script = engine.getOp("var").args("b[1]");
        Assertions.assertEquals("Wilcommen, Welt", script.evaluate(engine));
    }

    @Test
    void testVarIndexIndex() {
        final var engine = Engine.withData(Map.of("a", "Hello, World!", "b", List.of("german", List.of("Wilcommen, Welt"))));
        final var script = engine.getOp("var").args("b[1][0]");
        Assertions.assertEquals("Wilcommen, Welt", script.evaluate(engine));
    }

    @Test
    void testReflectiveAccess() {
        final var ctx = new MapContext(Map.of("a", this));
        ctx.convenience.doJavaIntrospection();
        final var engine = Engine.withData(ctx);
        final var script = engine.getOp("var").args("a.testField");
        Assertions.assertEquals("Hello, World!", script.evaluate(engine));
    }

    @Test
    void testReflectiveAccessFailWoRegistration() {
        final var ctx = new MapContext(Map.of("a", this));
        // DO NOT ctx.convenience.doJavaIntrospection();
        final var engine = Engine.withData(ctx);
        final var script = engine.getOp("var").args("a.testField");
        Assertions.assertThrows(IllegalArgumentException.class, ()-> script.evaluate(engine));
    }

    @Test
    void testReflectiveAccessNonExistingField() {
        final var ctx = new MapContext(Map.of("a", this));
        ctx.convenience.doJavaIntrospection();
        final var engine = Engine.withData(ctx);
        final var script = engine.getOp("var").args("a.testMedow");
        Assertions.assertThrows(IllegalArgumentException.class, () -> script.evaluate(engine));
    }

}

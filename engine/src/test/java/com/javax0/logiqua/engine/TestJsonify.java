package com.javax0.logiqua.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestJsonify {

    @Test
    void testJsonifyHelloWorld() {
        final var engine = Engine.withData(Map.of());
        final var script =
                engine.getOp("log").args(
                        engine.constant("Hello, \"World\"!"));
        Assertions.assertEquals("{\"log\":[\"Hello, \\\"World\\\"!\"]}",script.jsonify());
    }

    @Test
    void testJsonifyComplexExample() {
        final var engine = Engine.withData(Map.of());
        final var script =
                engine.getOp("+").args(
                        engine.constant(1),
                        engine.getOp("==").args(engine.constant(List.of(1,2,3)),engine.constant(Map.of("a",1)))


                );
        Assertions.assertEquals("{\"+\":[1,{\"==\":[[1,2,3],{\"a\":\"1\"}]}]}",script.jsonify());
    }
}

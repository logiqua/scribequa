package com.javax0.logiqua.engine;

import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestEngine {

    @Test
    void testHelloWorld() {
        final var engine = Engine.withData(Map.of());
        final var script =
                engine.getOp("log").args(
                        engine.constant("Hello, World!"));
        script.evaluate(engine);
    }
}

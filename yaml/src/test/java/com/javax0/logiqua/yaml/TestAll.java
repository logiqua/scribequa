package com.javax0.logiqua.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAll {

    @Test
    void testAllTrue() {
        final var script = """
                all:
                  - - 2
                    - 4
                    - 6
                    - 8
                  - "==":
                    - "%":
                      - var: ""
                      - 2
                    - 0
                """;
        final var scriptObject = new YamlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAllFalse() {
        final var script = """
                all:
                  - - 2
                    - 4
                    - 6
                    - 7
                  - "==":
                    - "%":
                      - var: ""
                      - 2
                    - 0
                """;
        final var scriptObject = new YamlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(false, result);
    }
}

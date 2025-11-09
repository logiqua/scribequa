package com.javax0.logiqua.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNone {

    @Test
    void testNoneTrue() {
        final var script = """
                none:
                  - - 1
                    - 3
                    - 5
                    - 7
                    - 9
                    - 11
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
    void testNoneFalse() {
        final var script = """
                none:
                  - - 1
                    - 3
                    - 5
                    - 8
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

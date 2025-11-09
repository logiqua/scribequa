package com.javax0.logiqua.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestReduce {
    @Test
    void testReduce() {
        final var script = """
                reduce:
                  - - 1
                    - 2
                    - 3
                    - 4
                    - 5
                  - "+":
                    - var: ""
                    - var: "accumulator"
                  - 0
                """;
        final var scriptObject = new YamlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(15L, result);
    }


}

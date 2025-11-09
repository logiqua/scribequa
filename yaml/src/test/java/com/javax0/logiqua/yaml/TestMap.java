package com.javax0.logiqua.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestMap {
    @Test
    void testMap() {
        final var script = """
                map:
                  - - 23
                    - 24
                  - "*":
                    - var: ""
                    - 2
                """;
        final var scriptObject = new YamlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(List.of(46L,48L), result);
    }


}

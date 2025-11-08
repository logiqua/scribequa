package com.javax0.logiqua.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestMap {
    private void test(final Object result, final String script) {
    }

    @Test
    void testMap() {
        final var script = """
                { "map" : [ [23, 24],
                            {"*" :[{"var" : "current"} , 2] }
                          ]
                }
                """;
        final var scriptObject = new JsonLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(List.of(46L,48L), result);
    }


}

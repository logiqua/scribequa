package com.javax0.logiqua.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestReduce {
    private void test(final Object result, final String script) {
    }

    @Test
    void testReduce() {
        final var script = """
                {"reduce":[
                    [1,2,3,4,5],
                    {"+":[{"var":"current"}, {"var":"accumulator"}]},
                    0
                ]}
                """;
        final var scriptObject = new JsonLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(15L, result);
    }


}

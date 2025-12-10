package com.javax0.logiqua.jsonlogic;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReduceExpressionTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void testReduce() throws JsonLogicException {
        String json = """
                {"reduce":[
                              {"var":"x"},
                              {"+":[{"var":"current"}, {"var":"accumulator"}]},
                              0
                          ]
                }
                """;
        final var data = Map.of("x",new int[]{1, 2, 3, 4, 5, 6});
        Object result = jsonLogic.apply(json, data);

        assertEquals(21L, result);
    }
}

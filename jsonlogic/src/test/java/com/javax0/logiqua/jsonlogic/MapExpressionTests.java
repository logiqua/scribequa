package com.javax0.logiqua.jsonlogic;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapExpressionTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void testMap() throws JsonLogicException {
        String json = """
                {"map": [
                          {"var": "data"},
                          {"*": [{"var": ""}, 2]}
                        ]
                }
                """;
        Integer[] data = new Integer[]{1, 2, 3};
        Object result = jsonLogic.apply(json, Map.of("data", data));

        assertEquals(List.of(2L, 4L, 6L).toString(), result.toString());
    }
}

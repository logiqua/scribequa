package com.javax0.logiqua.jsonlogic;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterExpressionTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void testMap() throws JsonLogicException {
        String json = """
                {"filter": [
                                    {"var": "data"},
                                    {"==": [{"%": [{"var": ""}, 2]}, 0]}
                                  ]}
                """;
        Integer[] data = new Integer[]{1, 2, 3, 4, 5, 6};
        Object result = jsonLogic.apply(json, Map.of("data", data));

        assertEquals(List.of(2, 4, 6), result);
    }
}

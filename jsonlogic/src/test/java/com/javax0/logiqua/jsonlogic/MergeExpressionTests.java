package com.javax0.logiqua.jsonlogic;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergeExpressionTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void testMerge() throws JsonLogicException {
        Object result = jsonLogic.apply("""
                {"merge": [[1, 2], [3, 4]]}
                """, null);

        assertEquals(List.of(1, 2, 3, 4).toString(), result.toString());
    }

    @Test
    public void testMergeWithNonArrays() throws JsonLogicException {
        Object result = jsonLogic.apply("""
                {"merge": [1, 2, [3, 4]]}
                """, null);

        assertEquals(List.of(1, 2, 3, 4).toString(), result.toString());
    }
}

package com.javax0.logiqua.jsonlogic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IfExpressionTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void testIfTrue() throws JsonLogicException {
        String json = """
                {"if" : [true, "yes", "no"]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals("yes", result);
    }

    @Test
    public void testIfFalse() throws JsonLogicException {
        String json = """
                {"if" : [false, "yes", "no"]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals("no", result);
    }

    @Test
    public void testIfElseIfElse() throws JsonLogicException {
        String json = """
                
                  {"if" : [
                            {"<": [50, 0]}, "freezing",
                            {"<": [50, 100]}, "liquid",
                            "gas"
                          ]
                  }
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals("liquid", result);
    }
}

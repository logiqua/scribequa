package com.javax0.logiqua.jsonlogic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MathExpressionTests {
    private static final JsonLogic jsonLogic = new JsonLogic();

    @Test
    public void testAdd() throws JsonLogicException {
        String json = """
                {"+":[4,2]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(6L, result);
    }

    @Test
    public void testMultiAdd() throws JsonLogicException {
        String json = """
                {"+":[2,2,2,2,2]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(10L, result);
    }

    @Test
    public void testSingleAdd() throws JsonLogicException {
        String json = """
                {"+" : "3.14"}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(3.14, result);
    }

    //@Test this is not defined in json.logic, it is just so in the reference implementation
    public void testAddWithArray() throws JsonLogicException {
        String json = """
                {"+":[2,[[3,4],5]]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(5L, result);  // This matches reference impl at jsonlogic.com
    }

    //@Test this is not defined in json.logic, it is just so in the reference implementation
    public void testStringAdd() throws JsonLogicException {
        assertNull(jsonLogic.apply("""
                {"+" : "foo"}
                """, null));
        assertNull(jsonLogic.apply("""
                {"+" : ["foo"]}
                """, null));
        assertNull(jsonLogic.apply("""
                {"+" : [1, "foo"]}
                """, null));
    }

    @Test
    public void testSubtract() throws JsonLogicException {
        String json = """
                {"-":[4,2]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(2L, result);
    }

    @Test
    public void testSingleSubtract() throws JsonLogicException {
        String json = """
                {"-": 2 }
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(-2L, result);
    }

    @Test
    public void testSingleSubtractString() throws JsonLogicException {
        String json = """
                {"-": "2" }
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(-2L, result);
    }

    @Test
    public void testMultiply() throws JsonLogicException {
        String json = """
                {"*":[4,2]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(8L, result);
    }

    @Test
    public void testMultiMultiply() throws JsonLogicException {
        String json = """
                {"*":[2,2,2,2,2]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(32L, result);
    }

    //@Test this is not defined in json.logic, it is just so in the reference implementation
    public void testMultiplyWithArray() throws JsonLogicException {
        String json = """
                {"*":[2,[[3, 4], 5]]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(6L, result);  // This matches reference impl at jsonlogic.com
    }

    //@Test this is not defined in json.logic, it is just so in the reference implementation
    public void testMultiplyWithEmptyArray() throws JsonLogicException {
        String json = """
                {"*":[2,[]]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertNull(result);  // This matches reference impl at jsonlogic.com
    }

    @Test
    public void testDivide() throws JsonLogicException {
        String json = """
                {"/":[4,2]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(2L, result);
    }

    @Test
    public void testDivideBy0() throws JsonLogicException {
        String json = """
                {"/":[4,0]}
                """;
        Object result = jsonLogic.apply(json, null);
        assertEquals(Double.POSITIVE_INFINITY, result);
    }

    @Test
    public void testFloatDivideBy0() throws JsonLogicException {
        String json = """
                {"/":[4.0,0.0]}
                """;
        Object result = jsonLogic.apply(json, null);
        assertEquals(Double.POSITIVE_INFINITY, result);
    }

    @Test
    public void testModulo() throws JsonLogicException {
        String json = """
                {"%": [101,2]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(1L, result);
    }

    @Test
    public void testMin() throws JsonLogicException {
        String json = """
                {"min":[1,2,3]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(1L, result);
    }

    @Test
    public void testMax() throws JsonLogicException {
        String json = """
                {"max":[1,2,3]}
                """;
        Object result = jsonLogic.apply(json, null);

        assertEquals(3L, result);
    }

    //@Test this is not defined in json.logic, it is just so in the reference implementation
    public void testDivideSingleNumber() throws JsonLogicException {
        assertEquals(null, jsonLogic.apply("""
                {"/": [0]}
                """, null));
    }
}

package com.javax0.logiqua.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestIn {
    private void test(final Object result, final String script) {
        final var scriptObject = new JsonLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testInTrue() {
        test(true, """
                {"in":[ "Ringo", ["John", "Paul", "George", "Ringo"] ]}
                """);
    }

    @Test
    void testInFalse() {
        test(false, """
                {"in":[ "Mozart", ["John", "Paul", "George", "Ringo"] ]}
                """);
    }

    @Test
    void testInTrueString() {
        test(true, """
                {"in":["field", "Springfield"]}
                """);
    }

    @Test
    void testInFalseString() {
        test(false, """
                {"in":["feld", "Springfield"]}
                """);
    }

}

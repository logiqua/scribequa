package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestIn {
    private void test(final Object result, final String script) {
        final var scriptObject = new ExpLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testInTrue() {
        test(true, """
                "Ringo" in ["John", "Paul", "George", "Ringo"]
                """);
    }

    @Test
    void testInTrueFunction() {
        test(true, """
                in("Ringo" , ["John", "Paul", "George", "Ringo"])
                """);
    }

    @Test
    void testInFalse() {
        test(false, """
                "Mozart" in ["John", "Paul", "George", "Ringo"]
                """);
    }

    @Test
    void testInTrueString() {
        test(true, """
                "field" in "Springfield"
                """);
    }

    @Test
    void testInFalseString() {
        test(false, """
                "feld" in "Springfield"
                """);
    }

}

package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSubstr {
    private void test(final Object result, final String script) {
        final var scriptObject = new ExpLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testSubstr() {
        test("logic", """
                substr ("jsonlogic" , 4)
                """);

        test("son", """
                substr ("jsonlogic"  ,1, 3)
                """);

        test("logic", """
                substr ("jsonlogic" , -5)
                """);

        test("log", """
                substr ("jsonlogic" , 4 ,-2)
                """);
    }


}

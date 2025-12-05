package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCat {
    private void test(final Object result, final String script) {
        final var scriptObject = new ExpLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testCat() {
        test("I love pie", """
                cat("I love", " pie")
                """);
    }

}

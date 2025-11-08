package com.javax0.logiqua.lsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCat {
    private void test(final Object result, final String script) {
        final var scriptObject = new LspLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testCat() {
        test("I love pie", """
                (cat "I love" " pie")
                """);
    }

}

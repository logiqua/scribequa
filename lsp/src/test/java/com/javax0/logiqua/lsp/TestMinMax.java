package com.javax0.logiqua.lsp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMinMax {
    private void test(final Object result, final String script) {
        final var scriptObject = new LspLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testMinMax() {
        test(23L, "(min 23 24)");
        test(24L, "(max 23 24)");
    }

    @Test
    void testMinMaxMultiple() {
        test(22L, "(min 23 24 22 32)");
        test(32L, "(max 23 24 22 32)");
    }


}

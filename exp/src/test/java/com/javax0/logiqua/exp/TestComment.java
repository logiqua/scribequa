package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestComment {

    @Test
    void testArrayAndFieldAccessExpression() {
        final var script = "55* # this is a comment, ignored\n 13";
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(715L, result);
    }


}

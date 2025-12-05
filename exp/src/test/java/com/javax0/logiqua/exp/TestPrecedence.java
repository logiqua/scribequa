package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPrecedence {


    @Test
    void testSimpleExpression(){
        final var script = "63*14+3";
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(885L, result);
    }

    @Test
    void testComplexExpression(){
        final var script = "(((((63*14)))))+((3))";
        final var scriptObject = new ExpLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(885L, result);
    }

}

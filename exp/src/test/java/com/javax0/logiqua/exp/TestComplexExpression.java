package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestComplexExpression {

    @Test
    void testItAll() {
        final var script = """
                credit > debt * ratio 
                """;
        final var data = Map.<String, Object>of("currency", "EUR", "debt", 44.0, "credit", 56, "ratio", 1.6);
        final var scriptObject = new ExpLogiqua().with(data).compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(false, result);
        Assertions.assertEquals("""
                {">":[{"var":["credit"]},{"*":[{"var":["debt"]},{"var":["ratio"]}]}]}\
                """,scriptObject.jsonify());
    }
}

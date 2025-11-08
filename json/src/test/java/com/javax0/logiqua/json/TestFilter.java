package com.javax0.logiqua.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestFilter {

    @Test
    void testFilter() {
        final var script = """
                { "filter" : [ [1,2,3,4,5,6,7,8,9],
                            {"==" : [{"%" : [{"var" : "current"},2]} , 0] }
                          ]
                }
                """;
        final var scriptObject = new JsonLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(List.of(2L,4L,6L,8L), result);
    }


}

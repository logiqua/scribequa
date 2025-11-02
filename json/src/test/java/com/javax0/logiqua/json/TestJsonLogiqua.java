package com.javax0.logiqua.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsonLogiqua {


    @Test
    void test() {
        final var sample = "{\"==\" : [ 3, 5] }";
        final var script = new JsonLogiqua().compile(sample);
        Assertions.assertEquals(false, script.evaluate());
    }

}

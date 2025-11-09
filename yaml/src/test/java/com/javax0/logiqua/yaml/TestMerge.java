package com.javax0.logiqua.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestMerge {
    private void test(final Object result, final String script) {
        final var scriptObject = new YamlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testmergeLists() {
        test(List.of(1L, 2L, 3L, 4L), """
                merge:
                  - - 1
                    - 2
                  - - 3
                    - 4
                """);
    }

    @Test
    void testmergeMixed() {
        test(List.of(1L, 2L, 3L, 4L), """
                merge:
                  - 1
                  - 2
                  - - 3
                    - 4
                """);
    }

}

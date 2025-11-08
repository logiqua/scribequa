package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCat {
    private void test(final Object result, final String script) {
        final var scriptObject = new XmlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testCat() {
        test("I love pie", """
                <op symbol="cat">
                  <constant string="I love"/>
                  <constant string=" pie"/>
                </op>
                """);
    }


}

package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMinMax {
    private void test(final Object result, final String script) {
        final var scriptObject = new XmlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testMinMax() {
        test(23L, """
                <op symbol="min">
                    <constant integer="23"/>
                    <constant integer="24"/>
                </op>
                """);
        test(24L, """
                <op symbol="max">
                    <constant integer="23"/>
                    <constant integer="24"/>
                </op>
                """);
    }

    @Test
    void testMinMaxMultiple() {
        test(22L, """
                <op symbol="min">
                    <constant integer="23"/>
                    <constant integer="24"/>
                    <constant integer="22"/>
                    <constant integer="32"/>
                </op>
                """);
        test(32L, """
                <op symbol="max">
                    <constant integer="23"/>
                    <constant integer="24"/>
                    <constant integer="22"/>
                    <constant integer="32"/>
                </op>
                """);
    }


}

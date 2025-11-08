package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSubstr {
    private void test(final Object result, final String script) {
        final var scriptObject = new XmlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testSubstr() {
        test("logic", """
                <op symbol="substr">
                    <constant string="jsonlogic"/>
                    <constant integer="4"/>
                </op>
                """);

        test("son", """
                <op symbol="substr">
                    <constant string="jsonlogic"/>
                    <constant integer="1"/>
                    <constant integer="3"/>
                </op>
                """);

        test("logic", """
                <op symbol="substr">
                    <constant string="jsonlogic"/>
                    <constant integer="-5"/>
                </op>
                """);

        test("log", """
                <op symbol="substr">
                    <constant string="jsonlogic"/>
                    <constant integer="4"/>
                    <constant integer="-2"/>
                </op>
                """);
    }


}

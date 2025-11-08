package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestIn {
    private void test(final Object result, final String script) {
        final var scriptObject = new XmlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testInTrue() {
        test(true, """
                <op symbol="in">
                    <constant string="Ringo"/>
                    <list>
                        <constant string="John"/>
                        <constant string="Paul"/>
                        <constant string="George"/>
                        <constant string="Ringo"/>
                    </list>
                </op>
                """);
    }

    @Test
    void testInFalse() {
        test(false, """
                <op symbol="in">
                    <constant string="Mozart"/>
                    <list>
                        <constant string="John"/>
                        <constant string="Paul"/>
                        <constant string="George"/>
                        <constant string="Ringo"/>
                    </list>
                </op>
                """);
    }

    @Test
    void testInTrueString() {
        test(true, """
                <op symbol="in">
                    <constant string="field"/>
                    <constant string="Springfield"/>
                </op>
                """);
    }

    @Test
    void testInFalseString() {
        test(false, """
                <op symbol="in">
                    <constant string="feld"/>
                    <constant string="Springfield"/>
                </op>
                """);
    }

}

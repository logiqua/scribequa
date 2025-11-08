package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNone {

    @Test
    void testNoneTrue() {
        final var script = """
                <op symbol="none">
                    <list>
                        <constant integer="1"/>
                        <constant integer="3"/>
                        <constant integer="5"/>
                        <constant integer="7"/>
                        <constant integer="9"/>
                        <constant integer="11"/>
                    </list>
                    <op symbol="==">
                        <op symbol="%">
                            <var id=""/>
                            <constant integer="2"/>
                        </op>
                        <constant integer="0"/>
                    </op>
                </op>
                """;
        final var scriptObject = new XmlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(true, result);
    }

    @Test
    void testNoneFalse() {
        final var script = """
                <op symbol="none">
                    <list>
                        <constant integer="1"/>
                        <constant integer="3"/>
                        <constant integer="5"/>
                        <constant integer="8"/>
                    </list>
                    <op symbol="==">
                        <op symbol="%">
                            <var id=""/>
                            <constant integer="2"/>
                        </op>
                        <constant integer="0"/>
                    </op>
                </op>
                """;
        final var scriptObject = new XmlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(false, result);
    }
}

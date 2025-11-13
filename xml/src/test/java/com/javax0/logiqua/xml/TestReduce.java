package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestReduce {

    @Test
    void testReduce() {
        final var script = """
                <op symbol="reduce">
                    <list>
                        <constant integer="1"/>
                        <constant integer="2"/>
                        <constant integer="3"/>
                        <constant integer="4"/>
                        <constant integer="5"/>
                    </list>
                    <op symbol="+">
                        <var id="current"/>
                        <var id="accumulator"/>
                    </op>
                    <constant integer="0"/>
                </op>
                """;
        final var scriptObject = new XmlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(15L, result);
    }


}

package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAll {

    @Test
    void testAllTrue() {
        final var script = """
                <op symbol="all">
                  <list>
                    <constant integer="2"/>
                    <constant integer="4"/>
                    <constant integer="6"/>
                    <constant integer="8"/>
                  </list>
                  <op symbol="==">
                    <op symbol="%" >
                      <op symbol="var"><![CDATA[]]></op>
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
    void testAllFalse() {
        final var script = """
                <op symbol="all">
                  <list>
                    <constant integer="2"/>
                    <constant integer="4"/>
                    <constant integer="6"/>
                    <constant integer="7"/>
                  </list>
                  <op symbol="==">
                    <op symbol="%" >
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

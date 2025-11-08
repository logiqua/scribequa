package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestMap {
    private void test(final Object result, final String script) {
    }

    @Test
    void testMap() {
        final var script = """
                <op symbol="map">
                    <list>
                        <constant integer="23"/>
                        <constant integer="24"/>
                    </list>
                    <op symbol="*">
                        <var id="current"/>
                        <constant integer="2"/>
                    </op>
                </op>
                """;
        final var scriptObject = new XmlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(List.of(46L,48L), result);
    }


}

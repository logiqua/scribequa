package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestFilter {

    @Test
    void testFilter() {
        final var script = """
                <op symbol="filter">
                    <list>
                        <constant integer="1"/>
                        <constant integer="2"/>
                        <constant integer="3"/>
                        <constant integer="4"/>
                        <constant integer="5"/>
                        <constant integer="6"/>
                        <constant integer="7"/>
                        <constant integer="8"/>
                        <constant integer="9"/>
                    </list>
                    <op symbol="==">
                        <op symbol="%" >
                            <var id="current"/>
                            <constant integer="2"/>
                        </op>
                        <constant integer="0"/>
                    </op>
                </op>
                """;
        final var scriptObject = new XmlLogiqua().compile(script);
        final var result = scriptObject.evaluate();
        Assertions.assertEquals(List.of(2L,4L,6L,8L), result);
    }


}

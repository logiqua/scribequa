package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestMerge {
    private void test(final Object result, final String script) {
        final var scriptObject = new XmlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    @Test
    void testmergeLists() {
        test(List.of(1L, 2L, 3L, 4L), """
                <op symbol="merge">
                    <list>
                        <constant integer="1"/>
                        <constant integer="2"/>
                    </list>
                    <list>
                        <constant integer="3"/>
                        <constant integer="4"/>
                    </list>
                </op>
                """);
    }

    @Test
    void testmergeMixed() {
        test(List.of(1L, 2L, 3L, 4L), """
                <op symbol="merge">
                    <constant integer="1"/>
                    <constant integer="2"/>
                    <list>
                        <constant integer="3"/>
                        <constant integer="4"/>
                    </list>
                </op>
                """);
    }

}

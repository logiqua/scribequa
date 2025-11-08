package com.javax0.logiqua.lsp;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.analyzers.SymbolAnalyzer;
import com.javax0.lex.tokens.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestLspReader {

    private static Object parseToList(String sample) {
        final var analyzer = new LexicalAnalyzer();
        analyzer.replaceAnalyzer(SymbolAnalyzer.class,new LispSymbolAnalyzer());
        analyzer.skip(Space.class);
        final var tokenArray = analyzer.analyse(StringInput.of(sample));
        final var tokens = TokenIterator.over(tokenArray);
        return LspReader.of(tokens).read();
    }

    @Test
    void readSamplelsp() {
        final var sample = """
                ("a" 1 "b" "Hello, World!")
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertEquals("[a, 1, b, Hello, World!]", lspObject.toString());
    }


    @Test
    void readEmptyObject() {
        final var sample = "()";
        final var lspObject = parseToList(sample);
        Assertions.assertEquals("[]", lspObject.toString());
    }

    @Test
    void readSingleProperty() {
        final var sample = """
                (name "John Cage")
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertEquals("John Cage", ((List<?>)lspObject).get(1).toString());
        Assertions.assertTrue( ((List<?>)lspObject).get(0).toString().contains("name"));
    }

    @Test
    void readMultipleStringProperties() {
        final var sample = """
                (("first" "Alice") ("middle" "Marie") ("last""Smith"))
                """;
        final var lspList = parseToList(sample);
        Assertions.assertTrue(lspList.toString().contains("[first, Alice]"));
        Assertions.assertTrue(lspList.toString().contains("[middle, Marie]"));
        Assertions.assertTrue(lspList.toString().contains("[last, Smith]"));
    }

    @Test
    void readMultipleNumericProperties() {
        final var sample = """
                ( ("x" 10) ("y" 20) ("z" 30))
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertTrue(lspObject.toString().contains("[x, 10]"));
        Assertions.assertTrue(lspObject.toString().contains("[y, 20]"));
        Assertions.assertTrue(lspObject.toString().contains("[z, 30]"));
    }

    @Test
    void readMixedTypes() {
        final var sample = """
                ( ("id" 42) ("active" true) ("name" "Test"))
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertTrue(lspObject.toString().contains("[id, 42]"));
        Assertions.assertTrue(lspObject.toString().contains("[active, true]"));
        Assertions.assertTrue(lspObject.toString().contains("[name, Test]"));
    }

    @Test
    void readBooleanTrue() {
        final var sample = """
                ("enabled" true)
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertTrue(lspObject.toString().contains("[enabled, true]"));
    }

    @Test
    void readBooleanFalse() {
        final var sample = """
                ("enabled" false)
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertTrue(lspObject.toString().contains("[enabled, false]"));
    }

    @Test
    void readNullValue() {
        final var sample = """
                ("value" null)
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertTrue(lspObject.toString().contains("[value, null]"));
    }

    @Test
    void readZeroValue() {
        final var sample = """
                (count 0)
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertEquals(0L,((List<?>)lspObject).get(1));
    }

    @Test
    void readNegativeNumber() {
        final var sample = """
                (temperature -15)
                """;
        final var lspObject = parseToList(sample);
        Assertions.assertTrue(lspObject.toString().contains("temperature"));
        Assertions.assertTrue(lspObject.toString().contains("-15"));
    }

}



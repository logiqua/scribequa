package com.javax0.lex;

import com.javax0.lex.analyzers.IntegerNumberAnalyzer;
import com.javax0.lex.analyzers.SymbolAnalyzer;
import com.javax0.lex.tokens.IntegerNumber;
import com.javax0.lex.tokens.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestLexicalAnalyzer {

    @Test
    void testLexicalAnalyzer() {
        final var input = StringInput.of("Hello, World");
        final var elements = new LexicalAnalyzer().analyse(input);
        final var result = getString(elements);
        Assertions.assertEquals("Identifier[start=null:0:0, end=null:0:5, value=Hello, lexeme=Hello]\n" +
                        "Symbol[start=null:0:5, end=null:0:6, value=,, lexeme=,]\n" +
                        "Space[start=null:0:6, end=null:0:7, value= , lexeme= ]\n" +
                        "Identifier[start=null:0:7, end=null:0:12, value=World, lexeme=World]\n"
                , result);
    }

    @Test
    void testSpacesSkipped() {
        final var input = StringInput.of("Hello, World");
        final var lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.skip(Space.class);
        final var elements = lexicalAnalyzer.analyse(input);
        final var result = getString(elements);
        Assertions.assertEquals("Identifier[start=null:0:0, end=null:0:5, value=Hello, lexeme=Hello]\n" +
                "Symbol[start=null:0:5, end=null:0:6, value=,, lexeme=,]\n" +
                "Identifier[start=null:0:7, end=null:0:12, value=World, lexeme=World]\n", result);
    }

    @Test
    void testSymbolsDefined() {
        final var input = StringInput.of("<< >> >>");
        final var lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.skip(Space.class);
        lexicalAnalyzer.getAnalyzer(SymbolAnalyzer.class).setSymbols(new String[]{"<<", ">>"});
        final var elements = lexicalAnalyzer.analyse(input);
        final var result = getString(elements);
        Assertions.assertEquals("Symbol[start=null:0:0, end=null:0:2, value=<<, lexeme=<<]\n" +
                "Symbol[start=null:0:3, end=null:0:5, value=>>, lexeme=>>]\n" +
                "Symbol[start=null:0:6, end=null:0:8, value=>>, lexeme=>>]\n", result);
    }

    @Test
    void testNewLineAnalyzer() {
        final var input = StringInput.of("{\"a\" : 613 }\n");
        final var lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.skip(Space.class);
        final var elements = lexicalAnalyzer.analyse(input);
        final var result = getString(elements);
        Assertions.assertEquals("Symbol[start=null:0:0, end=null:0:1, value={, lexeme={]\n" +
                "StringToken[start=null:0:1, end=null:0:3, value=a, lexeme=\"a\"]\n" +
                "Symbol[start=null:0:5, end=null:0:6, value=:, lexeme=:]\n" +
                "IntegerNumber[start=null:0:7, end=null:0:10, value=613, lexeme=613]\n" +
                "Symbol[start=null:0:11, end=null:0:12, value=}, lexeme=}]\n" +
                "NewLine[start=null:0:12, end=null:1:1, value=\n" +
                ", lexeme=\n" +
                "]\n", result);
    }

    @Test
    void testReplacedAnalyzer() {
        class MyAnalyzer implements Analyzer<Long> {

            @Override
            public IntegerNumber analyse(Input input) {
                if (input.eof()) {
                    return null;
                }
                final var start = input.position();
                var ch = input.next();
                if (Character.isDigit(ch)) {
                    final var end = input.position();
                    return new IntegerNumber(start, end, Long.parseLong(ch + ""), (ch + "").repeat(10));
                }
                return null;
            }
        }

        final var input = StringInput.of("{\"a\" : 613 }\n");
        final var lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.skip(Space.class);
        lexicalAnalyzer.replaceAnalyzer(IntegerNumberAnalyzer.class, new MyAnalyzer());
        final var elements = lexicalAnalyzer.analyse(input);
        final var result = getString(elements);
        Assertions.assertEquals("Symbol[start=null:0:0, end=null:0:1, value={, lexeme={]\n" +
                "StringToken[start=null:0:1, end=null:0:3, value=a, lexeme=\"a\"]\n" +
                "Symbol[start=null:0:5, end=null:0:6, value=:, lexeme=:]\n" +
                "IntegerNumber[start=null:0:7, end=null:0:8, value=6, lexeme=6666666666]\n" +
                "IntegerNumber[start=null:0:8, end=null:0:9, value=1, lexeme=1111111111]\n" +
                "IntegerNumber[start=null:0:9, end=null:0:10, value=3, lexeme=3333333333]\n" +
                "Symbol[start=null:0:11, end=null:0:12, value=}, lexeme=}]\n" +
                "NewLine[start=null:0:12, end=null:1:1, value=\n" +
                ", lexeme=\n" +
                "]\n", result);
    }

    private static String getString(Token<?>[] elements) {
        final var sb = new StringBuilder();
        for (var element : elements) {
            sb.append(element).append("\n");
        }
        return sb.toString();
    }

}

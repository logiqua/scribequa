package com.javax0.logiqua.jsonlogic;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.json.JsonReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompatibilityTest {

    @Test
    void testCompatibility() throws Exception {
        try (final var in = getClass().getClassLoader().getResourceAsStream("test.json")) {
            Assertions.assertNotNull(in);
            final var json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            final var lexer = new LexicalAnalyzer();
            lexer.skip(NewLine.class);
            lexer.skip(Space.class);
            final var tokens = lexer.analyse(StringInput.of(json));
            Object obj = JsonReader.of(TokenIterator.over(tokens)).read();
            if (!(obj instanceof List<?> list)) {
                throw new AssertionError("The result is not a list");
            }
            for( final var item : list ) {
                if(item instanceof List<?> itemList) {
                    final var command = itemList.getFirst();
                    final var data = itemList.get(1);
                    final var expected = itemList.getLast();
                    final var cmdString = toString(command);
                    final var result = new JsonLogic().apply(cmdString, data);
                    Assertions.assertEquals(expected, result,"test for '"+cmdString+"' on "+toString(data)+" has failed");
                }

            }
        }

    }

    private String toString(Object obj){
        return switch( obj ){
            case null -> "null";
            case String s -> "\""+s+"\"";
            case Map<?,?> map -> "{"+map.entrySet().stream().map(e -> toString(e.getKey())+" : "+toString(e.getValue())).collect(Collectors.joining(","))+"}";
            case List<?> list -> "["+list.stream().map(this::toString).collect(Collectors.joining(","))+"]";
            default -> obj.toString();
        };
    }

}



package com.javax0.logiqua.json;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.engine.Engine;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestJsonBuilder {


    Object  test(String sample){
        final var analyzer = new LexicalAnalyzer();
        analyzer.skip(Space.class);
        analyzer.skip(NewLine.class);
        final var tokenArray = analyzer.analyse(StringInput.of(sample));
        final var tokens = TokenIterator.over(tokenArray);
        final var json = JsonReader.of(tokens).read();

        final var engine = Engine.withData(Map.of());

        final var script = JsonBuilder.from(json,engine).build();
        return script.evaluate();
    }

    @Test
    void  test(){
        final var sample = """
                {"==" : [ 3, 5] }
                """;
        System.out.println(test(sample));
    }


}

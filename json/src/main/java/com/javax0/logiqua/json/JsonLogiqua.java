package com.javax0.logiqua.json;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.Logiqua;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;

import java.util.Map;

public class JsonLogiqua implements Logiqua {

    private Engine engine = null;

    public Engine engine() {
        return engine;
    }

    public JsonLogiqua with(Engine engine) {
        this.engine = engine;
        return this;
    }

    public JsonLogiqua with(Map<String,Object> data) {
        if( engine != null) {
            throw new IllegalStateException("The engine is already set");
        }
        this.engine = Engine.withData(data);
        return this;
    }

    @Override
    public Script compile(String source) {
        if( engine == null) {
            engine = Engine.withData(Map.of());
        }
        if( engine.limit() < source.length()) {
            throw new IllegalArgumentException("The source is too long");
        }
        final var analyzer = new LexicalAnalyzer();
        analyzer.skip(Space.class);
        analyzer.skip(NewLine.class);
        final var tokenArray = analyzer.analyse(StringInput.of(source));
        final var tokens = TokenIterator.over(tokenArray);
        final var json = JsonReader.of(tokens).read();


        return JsonBuilder.from(json, engine).build();
    }
}

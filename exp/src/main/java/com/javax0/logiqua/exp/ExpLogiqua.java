package com.javax0.logiqua.exp;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.analyzers.FloatNumberAnalyzer;
import com.javax0.lex.analyzers.IntegerNumberAnalyzer;
import com.javax0.lex.analyzers.SymbolAnalyzer;
import com.javax0.lex.tokens.Identifier;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.Logiqua;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.engine.MapContext;

import java.util.Map;

public class ExpLogiqua implements Logiqua {

    private Engine engine = null;

    public Engine engine() {
        return engine;
    }

    public ExpLogiqua with(Engine engine) {
        this.engine = engine;
        return this;
    }

    public ExpLogiqua with(Map<String, Object> data) {
        if (engine != null) {
            throw new IllegalStateException("The engine is already set");
        }
        this.engine = Engine.withData(data);

        return this;
    }

    @Override
    public Script compile(String source) {
        if (engine == null) {
            engine = Engine.withData(Map.of());
        }
        if( engine.limit() < source.length()) {
            throw new IllegalArgumentException("The source is too long");
        }
        final var analyzer = new LexicalAnalyzer();
        analyzer.skip(Space.class);
        analyzer.skip(NewLine.class);
        analyzer.skip(ExpCommentAnalyzer.Comment.class);
        analyzer.replaceAnalyzer(SymbolAnalyzer.class, new ExpSymbolAnalyzer());
        analyzer.replaceAnalyzer(FloatNumberAnalyzer.class, new ExpFloatNumberAnalyzer());
        analyzer.replaceAnalyzer(IntegerNumberAnalyzer.class, new ExpIntegerNumberAnalyzer());
        analyzer.registerAnalyzer(new ExpCommentAnalyzer());
        final var tokenArray = analyzer.analyse(StringInput.of(source));
        final var tokens = TokenIterator.over(tokenArray);
        final var json = ExpReader.of(tokens).read();
        if (!tokens.eof()) {
            throw new IllegalArgumentException("There is extra text following the script");
        }
        ((MapContext) engine.getContext()).registerCaster(Identifier.class, String.class, token -> token.value());
        return ExpBuilder.from(json, engine).build();
    }
}

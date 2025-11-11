package com.javax0.logiqua.jsonlogic;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.json.JsonLogiqua;
import com.javax0.logiqua.json.JsonReader;
import com.javax0.logiqua.jsonlogic.compatibilitycommands.*;

import java.lang.reflect.Array;
import java.util.Collection;

public class JsonLogic {

    private void registerCompatibilityOperations(JsonLogiqua jlEngine) {
        final var engine = jlEngine.engine();
        engine.updateOperation(new JLOr());
        engine.updateOperation(new JLAnd());
        engine.updateOperation(new JLIf());
        engine.updateOperation(new JLIn());
        engine.updateOperation(new JLSome());
        engine.updateOperation(new JLNone());
        engine.updateOperation(new JLVar());
        engine.updateOperation(new JLNot());
        engine.registerOperation(new JLNotNot());
        engine.updateOperation(new JLEqual());
        engine.updateOperation(new JLSubstr());
        engine.registerOperation(new JLStrictEqual());
        engine.registerOperation(new JLStrictInEqual());
        engine.updateOperation(new JLAll());
        engine.updateOperation(new JLTernary());
        engine.updateOperation(new JLMultiply());
        engine.updateOperation(new JLFilter());
        final var mapContext = ((CompatibilityContext) engine.getContext()).mapContext;
        mapContext.registerCaster(String.class, Number.class, s -> {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                return Double.parseDouble(s);
            }
        });
        mapContext.registerCaster(String.class, Boolean.class, JsonLogic::truthy);
        mapContext.registerCaster(Number.class, Boolean.class, JsonLogic::truthy);
    }

    public Object apply(String json, Object data) throws JsonLogicException {
        if (data instanceof String string) {
            final var analyzer = new LexicalAnalyzer();
            analyzer.skip(Space.class);
            analyzer.skip(NewLine.class);
            final var tokenArray = analyzer.analyse(StringInput.of(string));
            final var tokens = TokenIterator.over(tokenArray);
            final var dataMap = JsonReader.of(tokens).read();
            return apply(json, dataMap);
        }
        final var map = new CompatibilityContext(data);
        final var engine = Engine.withData(map);
        final var jsl = new JsonLogiqua().with(engine);
        registerCompatibilityOperations(jsl);
        final var scriptObject = jsl.compile(json);
        return scriptObject.evaluate();
    }


    public static boolean truthy(Object value) {
        if (value == null) {
            return false;
        }

        if (value instanceof Boolean) {
            return (boolean) value;
        }

        if (value instanceof Number) {
            if (value instanceof Double) {
                Double d = (Double) value;

                if (d.isNaN()) {
                    return false;
                } else if (d.isInfinite()) {
                    return true;
                }
            }

            if (value instanceof Float) {
                Float f = (Float) value;

                if (f.isNaN()) {
                    return false;
                } else if (f.isInfinite()) {
                    return true;
                }
            }

            return ((Number) value).doubleValue() != 0.0;
        }

        if (value instanceof String) {
            return !((String) value).isEmpty();
        }

        if (value instanceof Collection) {
            return !((Collection<?>) value).isEmpty();
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        }

        return true;
    }


}

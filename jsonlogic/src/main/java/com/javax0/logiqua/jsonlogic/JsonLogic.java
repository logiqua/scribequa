package com.javax0.logiqua.jsonlogic;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.Schema;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.engine.SchemaCheckedContext;
import com.javax0.logiqua.json.JsonLogiqua;
import com.javax0.logiqua.json.JsonReader;
import com.javax0.logiqua.jsonlogic.compatibilitycommands.*;

import java.lang.reflect.Array;
import java.util.Collection;

public class JsonLogic {

    private void registerCompatibilityOperations(JsonLogiqua jlEngine, CompatibilityContext context) {
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
        engine.updateOperation(new JLDivide());
        final var mapContext = context.mapContext;
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

    /**
     * Apply a JsonLogic rule to the data.
     *
     * @param json the JsonLogic rule
     * @param data the data the rule reads, either a Java structure or a JSON string
     * @return the result of the rule
     */
    public Object apply(String json, Object data) {
        return apply(json, data, null);
    }

    /**
     * Apply a JsonLogic rule to the data, checking every variable read against a schema of the data.
     * <p>
     * JsonLogic itself has no notion of an undefined variable. {@code {"var": "user.nmae"}} quietly
     * evaluates to {@code null}, and a typo in a rule can sit unnoticed for a long time. With a schema
     * the same rule throws a {@link com.javax0.logiqua.SchemaViolationException}, while a field that
     * the schema does describe and the data merely lacks keeps returning {@code null} or the default,
     * as JsonLogic requires.
     * <p>
     * The paths the schema judges are the ones the rule writes, relative to the data, so the schema is
     * the schema of the {@code data} argument.
     *
     * @param json   the JsonLogic rule
     * @param data   the data the rule reads, either a Java structure or a JSON string
     * @param schema the schema of the data, or {@code null} for the unchecked behaviour
     * @return the result of the rule
     */
    public Object apply(String json, Object data, Schema schema) {
        if (data instanceof String string) {
            final var analyzer = new LexicalAnalyzer();
            analyzer.skip(Space.class);
            analyzer.skip(NewLine.class);
            final var tokenArray = analyzer.analyse(StringInput.of(string));
            final var tokens = TokenIterator.over(tokenArray);
            final var dataMap = JsonReader.of(tokens).read();
            return apply(json, dataMap, schema);
        }
        final var compatibilityContext = new CompatibilityContext(data);
        final var engine = Engine.withData(
                schema == null ? compatibilityContext : new SchemaCheckedContext(compatibilityContext, schema));
        final var jsl = new JsonLogiqua().with(engine);
        registerCompatibilityOperations(jsl, compatibilityContext);
        final var scriptObject = jsl.compile(json);
        return scriptObject.evaluate();
    }


    public static boolean truthy(Object value) {
        switch (value) {
            case null -> {
                return false;
            }
            case Boolean b -> {
                return (boolean) value;
            }
            case Number number -> {
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

                return number.doubleValue() != 0.0;
            }
            case String s -> {
                return !s.isEmpty();
            }
            case Collection<?> collection -> {
                return !collection.isEmpty();
            }
            default -> {
            }
        }

        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0;
        }

        return true;
    }


}

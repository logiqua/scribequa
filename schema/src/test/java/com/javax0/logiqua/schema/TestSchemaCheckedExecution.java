package com.javax0.logiqua.schema;

import com.javax0.logiqua.Schema;
import com.javax0.logiqua.SchemaViolationException;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.engine.MapContext;
import com.javax0.logiqua.engine.SchemaCheckedContext;
import com.javax0.logiqua.json.JsonLogiqua;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * The schema seen from where it matters, a script reading a variable.
 */
public class TestSchemaCheckedExecution {

    private static final String SCHEMA = """
            {
              "type": "object",
              "additionalProperties": false,
              "properties": {
                "user": {
                  "type": "object",
                  "additionalProperties": false,
                  "properties": {
                    "name": { "type": "string" },
                    "age": { "type": "integer" }
                  }
                },
                "orders": {
                  "type": "array",
                  "items": {
                    "type": "object",
                    "additionalProperties": false,
                    "properties": {
                      "id": { "type": "string" },
                      "total": { "type": "number" }
                    }
                  }
                },
                "extras": { "type": "object" }
              }
            }
            """;

    private static Map<String, Object> data() {
        return Map.of(
                "user", Map.of("name", "Alice"),
                "orders", List.of(
                        Map.of("id", "o1", "total", 10L),
                        Map.of("id", "o2", "total", 20L)),
                "extras", Map.of("anything", "goes"));
    }

    private static Object evaluate(String script, Schema schema) {
        return new JsonLogiqua().with(Engine.withData(data(), schema)).compile(script).evaluate();
    }

    @Test
    void testDescribedVariableIsRead() {
        Assertions.assertEquals("Alice", evaluate("""
                {"var": "user.name"}""", JsonSchema.of(SCHEMA)));
    }

    @Test
    void testDescribedButAbsentVariableFallsBackToTheDefault() {
        Assertions.assertEquals("unknown", evaluate("""
                {"var": ["user.age", "unknown"]}""", JsonSchema.of(SCHEMA)));
    }

    @Test
    void testUndescribableVariableThrows() {
        final var exception = Assertions.assertThrows(SchemaViolationException.class, () -> evaluate("""
                {"var": "user.nmae"}""", JsonSchema.of(SCHEMA)));
        Assertions.assertEquals("user.nmae", exception.path());
        Assertions.assertTrue(exception.getMessage().contains("nmae"), exception.getMessage());
    }

    @Test
    void testTheDefaultValueDoesNotHideAnUndescribableVariable() {
        Assertions.assertThrows(SchemaViolationException.class, () -> evaluate("""
                {"var": ["user.nmae", "fallback"]}""", JsonSchema.of(SCHEMA)));
    }

    @Test
    void testWithoutASchemaTheDefaultValueIsReturned() {
        final var result = new JsonLogiqua().with(Engine.withData(data())).compile("""
                {"var": ["user.nmae", "fallback"]}""").evaluate();
        Assertions.assertEquals("fallback", result);
    }

    @Test
    void testUndescribableVariableThrowsEvenWhenTheDataHappensToHaveIt() {
        final var data = Map.<String, Object>of("user", Map.of("name", "Alice", "nmae", "typo"));
        final var engine = Engine.withData(data, JsonSchema.of(SCHEMA));
        final var script = new JsonLogiqua().with(engine).compile("""
                {"var": "user.nmae"}""");
        Assertions.assertThrows(SchemaViolationException.class, script::evaluate);
    }

    @Test
    void testAScalarCannotBeIndexed() {
        Assertions.assertThrows(SchemaViolationException.class, () -> evaluate("""
                {"var": "user.name.first"}""", JsonSchema.of(SCHEMA)));
    }

    @Test
    void testAnUnconstrainedPathIsAllowedWhenLenient() {
        Assertions.assertEquals("goes", evaluate("""
                {"var": "extras.anything"}""", JsonSchema.of(SCHEMA)));
    }

    @Test
    void testAnUnconstrainedPathIsRejectedWhenStrict() {
        final var context = new SchemaCheckedContext(
                new MapContext(data()), JsonSchema.of(SCHEMA), SchemaCheckedContext.Strictness.STRICT);
        final var script = new JsonLogiqua().with(Engine.withData(context)).compile("""
                {"var": ["extras.anything", "x"]}""");
        Assertions.assertThrows(SchemaViolationException.class, script::evaluate);
    }

    @Test
    void testLoopBodiesStillResolveAgainstTheCurrentElement() {
        Assertions.assertEquals(List.of(10L, 20L), evaluate("""
                {"map": [{"var": "orders"}, {"var": "total"}]}""", JsonSchema.of(SCHEMA)));
    }

    @Test
    void testIndexedElementPropertyIsChecked() {
        Assertions.assertEquals(20L, evaluate("""
                {"var": "orders[1].total"}""", JsonSchema.of(SCHEMA)));
        Assertions.assertThrows(SchemaViolationException.class, () -> evaluate("""
                {"var": "orders[1].totl"}""", JsonSchema.of(SCHEMA)));
    }

    @Test
    void testAPathSchemaWorksTheSameWay() {
        final var schema = PathSchema.of("user.name", "orders[*].total", "extras.**");
        Assertions.assertEquals("Alice", evaluate("""
                {"var": "user.name"}""", schema));
        Assertions.assertThrows(SchemaViolationException.class, () -> evaluate("""
                {"var": ["user.age", "unknown"]}""", schema));
    }

    @Test
    void testASchemaViolationIsNotAnIllegalArgumentException() {
        Assertions.assertFalse(IllegalArgumentException.class.isAssignableFrom(SchemaViolationException.class));
    }
}

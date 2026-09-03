package com.javax0.logiqua.schema;

import com.javax0.logiqua.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsonSchema {

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
                    "age": { "type": "integer" },
                    "address": { "$ref": "#/$defs/address" }
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
                "tags": { "type": "array", "items": { "type": "string" } },
                "extras": { "type": "object" }
              },
              "$defs": {
                "address": {
                  "type": "object",
                  "additionalProperties": false,
                  "properties": {
                    "city": { "type": "string" },
                    "zip": { "type": "string" }
                  }
                }
              }
            }
            """;

    private static Schema.Verdict verdict(String path) {
        return JsonSchema.of(SCHEMA).verdict(path);
    }

    @Test
    void testDescribedProperty() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, verdict("user.name"));
    }

    @Test
    void testMistypedPropertyIsImpossible() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, verdict("user.nmae"));
    }

    @Test
    void testMistypedRootPropertyIsImpossible() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, verdict("usr"));
    }

    @Test
    void testEmptyPathDescribesTheWholeData() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, verdict(""));
    }

    @Test
    void testIntermediateNodeIsDescribed() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, verdict("user"));
    }

    @Test
    void testReferencedSubSchemaIsFollowed() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, verdict("user.address.city"));
    }

    @Test
    void testMistypedPropertyOfReferencedSubSchemaIsImpossible() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, verdict("user.address.country"));
    }

    @Test
    void testArrayElementProperty() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, verdict("orders[0].total"));
    }

    @Test
    void testMistypedArrayElementPropertyIsImpossible() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, verdict("orders[3].totl"));
    }

    @Test
    void testArrayElementItselfIsDescribed() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, verdict("orders[17]"));
    }

    @Test
    void testDottedIndexIsTheSameAsBracketedIndex() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, verdict("orders.0.total"));
    }

    @Test
    void testPropertyNameOnAnArrayIsImpossible() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, verdict("orders.total"));
    }

    @Test
    void testScalarCannotBeIndexed() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, verdict("tags[0].length"));
    }

    @Test
    void testScalarCannotHaveAProperty() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, verdict("user.age.years"));
    }

    @Test
    void testObjectWithoutPropertiesIsUnconstrained() {
        Assertions.assertEquals(Schema.Verdict.UNCONSTRAINED, verdict("extras.whatever.deeply.nested"));
    }

    @Test
    void testTheReasonNamesTheOffendingSegment() {
        final var judgement = JsonSchema.of(SCHEMA).judge("user.address.country");
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, judgement.verdict());
        Assertions.assertTrue(judgement.reason().contains("country"), judgement.reason());
        Assertions.assertTrue(judgement.reason().contains("user.address"), judgement.reason());
    }

    @Test
    void testOpenObjectPermitsAnyPropertyByDefault() {
        final var schema = JsonSchema.of("""
                { "type": "object", "properties": { "name": { "type": "string" } } }
                """);
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("name"));
        Assertions.assertEquals(Schema.Verdict.UNCONSTRAINED, schema.verdict("nmae"));
    }

    @Test
    void testClosedObjectRejectsAnUndescribedProperty() {
        final var schema = JsonSchema.of("""
                { "type": "object", "properties": { "name": { "type": "string" } } }
                """).closed();
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("name"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("nmae"));
    }

    @Test
    void testClosedDoesNotRejectWhereTheSchemaDescribesNothing() {
        final var schema = JsonSchema.of("""
                { "type": "object" }
                """).closed();
        Assertions.assertEquals(Schema.Verdict.UNCONSTRAINED, schema.verdict("anything"));
    }

    @Test
    void testExplicitAdditionalPropertiesSchemaWins() {
        final var schema = JsonSchema.of("""
                {
                  "type": "object",
                  "properties": { "name": { "type": "string" } },
                  "additionalProperties": { "type": "object", "additionalProperties": false,
                                            "properties": { "code": { "type": "string" } } }
                }
                """).closed();
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("other.code"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("other.codes"));
    }

    @Test
    void testPatternPropertiesAreMatched() {
        final var schema = JsonSchema.of("""
                {
                  "type": "object",
                  "additionalProperties": false,
                  "patternProperties": {
                    "^x-": { "type": "object", "additionalProperties": false,
                             "properties": { "value": { "type": "string" } } }
                  }
                }
                """);
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("x-custom.value"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("x-custom.values"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("y-custom"));
    }

    @Test
    void testAnyOfBranchesAreUnioned() {
        final var schema = JsonSchema.of("""
                {
                  "anyOf": [
                    { "type": "object", "additionalProperties": false,
                      "properties": { "circle": { "type": "object", "additionalProperties": false,
                                                  "properties": { "radius": { "type": "number" } } } } },
                    { "type": "object", "additionalProperties": false,
                      "properties": { "square": { "type": "object", "additionalProperties": false,
                                                  "properties": { "side": { "type": "number" } } } } }
                  ]
                }
                """);
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("circle.radius"));
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("square.side"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("triangle.base"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("circle.diameter"));
    }

    @Test
    void testPrefixItemsFormATuple() {
        final var schema = JsonSchema.of("""
                {
                  "type": "object",
                  "additionalProperties": false,
                  "properties": {
                    "point": {
                      "type": "array",
                      "prefixItems": [ { "type": "number" }, { "type": "number" } ],
                      "items": false
                    }
                  }
                }
                """);
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("point[1]"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("point[2]"));
    }

    @Test
    void testMaxItemsBoundsTheIndex() {
        final var schema = JsonSchema.of("""
                { "type": "array", "maxItems": 2, "items": { "type": "string" } }
                """);
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("1"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("2"));
    }

    @Test
    void testBooleanFalseSchemaExcludesEverything() {
        final var schema = JsonSchema.of("""
                { "type": "object", "properties": { "gone": false }, "additionalProperties": false }
                """);
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("gone"));
    }

    @Test
    void testRecursiveReferenceTerminates() {
        final var schema = JsonSchema.of("""
                {
                  "$ref": "#/$defs/node",
                  "$defs": {
                    "node": {
                      "type": "object",
                      "additionalProperties": false,
                      "properties": { "name": { "type": "string" }, "child": { "$ref": "#/$defs/node" } }
                    }
                  }
                }
                """);
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, schema.verdict("child.child.child.name"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, schema.verdict("child.child.parent"));
    }

    @Test
    void testUnresolvableReferenceIsPermissive() {
        final var schema = JsonSchema.of("""
                { "type": "object", "additionalProperties": false,
                  "properties": { "external": { "$ref": "https://example.com/other.json" } } }
                """);
        Assertions.assertEquals(Schema.Verdict.UNCONSTRAINED, schema.verdict("external.anything"));
    }

    @Test
    void testAtNarrowsToASubStructure() {
        final var orderSchema = JsonSchema.of(SCHEMA).at("orders[0]");
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, orderSchema.verdict("total"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, orderSchema.verdict("totl"));
    }
}

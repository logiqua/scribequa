package com.javax0.logiqua.schema;

import com.javax0.logiqua.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPathSchema {

    private static final PathSchema SCHEMA = PathSchema.of(
            "user.name",
            "user.address.*",
            "orders[*].total",
            "extras.**");

    @Test
    void testExactPathIsDescribed() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, SCHEMA.verdict("user.name"));
    }

    @Test
    void testUndescribedPathIsImpossible() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, SCHEMA.verdict("user.nmae"));
    }

    @Test
    void testIntermediateNodeIsDescribed() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, SCHEMA.verdict("user"));
    }

    @Test
    void testEmptyPathIsDescribed() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, SCHEMA.verdict(""));
    }

    @Test
    void testSingleStarMatchesOneSegment() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, SCHEMA.verdict("user.address.city"));
    }

    @Test
    void testSingleStarDoesNotMatchTwoSegments() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, SCHEMA.verdict("user.address.city.zip"));
    }

    @Test
    void testStarMatchesAnArrayIndex() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, SCHEMA.verdict("orders[7].total"));
    }

    @Test
    void testMistypedPropertyAfterAnIndexIsImpossible() {
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, SCHEMA.verdict("orders[7].totl"));
    }

    @Test
    void testDoubleStarOpensTheWholeSubtree() {
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, SCHEMA.verdict("extras.a.b.c.d"));
    }

    @Test
    void testNothingIsUnconstrained() {
        Assertions.assertNotEquals(Schema.Verdict.UNCONSTRAINED, SCHEMA.verdict("anything.at.all"));
    }

    @Test
    void testAndAddsPaths() {
        final var extended = SCHEMA.and("audit.createdAt");
        Assertions.assertEquals(Schema.Verdict.DESCRIBED, extended.verdict("audit.createdAt"));
        Assertions.assertEquals(Schema.Verdict.IMPOSSIBLE, SCHEMA.verdict("audit.createdAt"));
    }

    @Test
    void testTheReasonNamesThePath() {
        final var judgement = SCHEMA.judge("user.nmae");
        Assertions.assertTrue(judgement.reason().contains("user.nmae"), judgement.reason());
    }
}

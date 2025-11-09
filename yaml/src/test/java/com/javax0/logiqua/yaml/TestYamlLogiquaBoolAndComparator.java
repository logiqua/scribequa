package com.javax0.logiqua.yaml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestYamlLogiquaBoolAndComparator {


    private void test(final Object result, final String script) {
        final var scriptObject = new YamlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    private void testFail(final String script) {
        Assertions.assertThrows(Exception.class, () -> {
            final var scriptObject = new YamlLogiqua().compile(script);
            scriptObject.evaluate();
        });
    }

    @Test
    void testEquals() {
        test(false, """
                "==":
                  - 3
                  - 5
                """);
    }


    @Test
    void testLessThan() {
        // Two arguments - true case
        test(true, """
                "<":
                  - 3
                  - 5
                """);

        // Two arguments - false case
        test(false, """
                "<":
                  - 5
                  - 3
                """);

        // Two arguments - equal (false)
        test(false, """
                "<":
                  - 5
                  - 5
                """);
    }

    @Test
    void testLessThanMultipleArguments() {
        // Three arguments - all true
        test(true, """
                "<":
                  - 1
                  - 3
                  - 5
                """);

        // Three arguments - middle comparison fails
        test(false, """
                "<":
                  - 1
                  - 5
                  - 3
                """);

        // Four arguments - all true
        test(true, """
                "<":
                  - 1
                  - 2
                  - 3
                  - 4
                """);
    }

    @Test
    void testLessThanInvalidArguments() {
        // No arguments
        testFail("""
                "<": []
                """);

        // Single argument
        testFail("""
                "<":
                  - 5
                """);
    }

    @Test
    void testGreaterThan() {
        // Two arguments - true case
        test(true, """
                ">":
                  - 5
                  - 3
                """);

        // Two arguments - false case
        test(false, """
                ">":
                  - 3
                  - 5
                """);

        // Two arguments - equal (false)
        test(false, """
                ">":
                  - 5
                  - 5
                """);
    }

    @Test
    void testGreaterThanMultipleArguments() {
        // Three arguments - all true
        test(true, """
                ">":
                  - 5
                  - 3
                  - 1
                """);

        // Three arguments - middle comparison fails
        test(false, """
                ">":
                  - 5
                  - 1
                  - 3
                """);

        // Four arguments - all true
        test(true, """
                ">":
                  - 10
                  - 8
                  - 6
                  - 4
                """);
    }

    @Test
    void testGreaterThanInvalidArguments() {
        // No arguments
        testFail("""
                ">": []
                """);

        // Single argument
        testFail("""
                ">":
                  - 5
                """);
    }

    @Test
    void testNotEqual() {
        // Two arguments - true case
        test(true, """
                "!=":
                  - 3
                  - 5
                """);

        // Two arguments - false case (equal)
        test(false, """
                "!=":
                  - 5
                  - 5
                """);
    }

    @Test
    void testNotEqualMultipleArguments() {
        // Three arguments - all different
        test(true, """
                "!=":
                  - 1
                  - 2
                  - 3
                """);

        // Three arguments - two equal
        test(false, """
                "!=":
                  - 1
                  - 2
                  - 1
                """);

        // Four arguments - all different
        test(true, """
                "!=":
                  - 1
                  - 2
                  - 3
                  - 4
                """);

        // Four arguments - one duplicate
        test(true, """
                "!=":
                  - 1
                  - 2
                  - 3
                  - 2
                """);
    }

    @Test
    void testNotEqualInvalidArguments() {
        // No arguments
        testFail("""
                "!=": []
                """);

        // Single argument
        testFail("""
                "!=":
                  - 5
                """);
    }

    @Test
    void testLessThanOrEqual() {
        // Two arguments - true case (less than)
        test(true, """
                "<=":
                  - 3
                  - 5
                """);

        // Two arguments - true case (equal)
        test(true, """
                "<=":
                  - 5
                  - 5
                """);

        // Two arguments - false case
        test(false, """
                "<=":
                  - 5
                  - 3
                """);
    }

    @Test
    void testLessThanOrEqualMultipleArguments() {
        // Three arguments - all true (ascending)
        test(true, """
                "<=":
                  - 1
                  - 3
                  - 5
                """);

        // Three arguments - with equality
        test(true, """
                "<=":
                  - 1
                  - 1
                  - 5
                """);

        // Three arguments - fails
        test(false, """
                "<=":
                  - 1
                  - 5
                  - 3
                """);

        // Four arguments - all equal
        test(true, """
                "<=":
                  - 5
                  - 5
                  - 5
                  - 5
                """);
    }

    @Test
    void testLessThanOrEqualInvalidArguments() {
        // No arguments
        testFail("""
                "<=": []
                """);

        // Single argument
        testFail("""
                "<=":
                  - 5
                """);
    }

    @Test
    void testGreaterThanOrEqual() {
        // Two arguments - true case (greater than)
        test(true, """
                ">=":
                  - 5
                  - 3
                """);

        // Two arguments - true case with >=
        test(true, """
                ">=":
                  - 5
                  - 5
                """);
    }

    @Test
    void testGreaterThanOrEqualMultipleArguments() {
        // Three arguments - all true (descending)
        test(true, """
                ">=":
                  - 5
                  - 3
                  - 1
                """);

        // Three arguments - with equality
        test(true, """
                ">=":
                  - 5
                  - 5
                  - 1
                """);

        // Three arguments - fails
        test(false, """
                ">=":
                  - 5
                  - 1
                  - 3
                """);

        // Four arguments - all equal
        test(true, """
                ">=":
                  - 5
                  - 5
                  - 5
                  - 5
                """);
    }

    @Test
    void testGreaterThanOrEqualInvalidArguments() {
        // No arguments
        testFail("""
                ">=": []
                """);

        // Single argument
        testFail("""
                ">=":
                  - 5
                """);
    }

    @Test
    void testNot() {
        // Single argument - true case
        test(false, """
                "!":
                  - true
                """);

        // Single argument - false case
        test(true, """
                "!":
                  - false
                """);
        test(false, """
                "!":
                  - true
                """);

        // Single argument - false case
        test(true, """
                "!":
                  - false
                """);

        // Single argument - truthy value
        testFail("""
                "!":
                  - 1
                """);

        // Single argument - falsy value
        testFail("""
                "!":
                  - 0
                """);

        // Single argument - null
        testFail("""
                "!":
                  - null
                """);
    }

    @Test
    void testNotInvalidArguments() {
        // No arguments
        testFail("""
                "!": []
                """);

        // Multiple arguments
        testFail("""
                "!":
                  - true
                  - false
                """);
    }

    @Test
    void testAnd() {
        // Two arguments - both true
        test(true, """
                and:
                  - true
                  - true
                """);

        // Two arguments - first false
        test(false, """
                and:
                  - false
                  - true
                """);

        // Two arguments - second false
        test(false, """
                and:
                  - true
                  - false
                """);

        // Two arguments - both false
        test(false, """
                and:
                  - false
                  - false
                """);
    }

    @Test
    void testAndMultipleArguments() {
        // Three arguments - all true
        test(true, """
                and:
                  - true
                  - true
                  - true
                """);

        // Three arguments - one false at start
        test(false, """
                and:
                  - false
                  - true
                  - true
                """);

        // Three arguments - one false in middle
        test(false, """
                and:
                  - true
                  - false
                  - true
                """);

        // Three arguments - one false at end
        test(false, """
                and:
                  - true
                  - true
                  - false
                """);

        // Four arguments - all true
        test(true, """
                and:
                  - true
                  - true
                  - true
                  - true
                """);

        // Four arguments - one false
        test(false, """
                and:
                  - true
                  - true
                  - false
                  - true
                """);
    }

    @Test
    void testAndWithTruthyFalsyValues() {
        // Truthy and falsy values
        testFail("""
                and:
                  - 1
                  - 2
                  - 3
                """);
        testFail("""
                and:
                  - 1
                  - 0
                  - 3
                """);
        testFail("""
                and:
                  - 1
                  - null
                  - 3
                """);
    }

    @Test
    void testAndInvalidArguments() {
        // No arguments
        testFail("""
                and: []
                """);

        // Single argument
        testFail("""
                and:
                  - true
                """);
    }

    @Test
    void testOr() {
        // Two arguments - both true
        test(true, """
                or:
                  - true
                  - true
                """);

        // Two arguments - first true
        test(true, """
                or:
                  - true
                  - false
                """);

        // Two arguments - second true
        test(true, """
                or:
                  - false
                  - true
                """);

        // Two arguments - both false
        test(false, """
                or:
                  - false
                  - false
                """);
    }

    @Test
    void testOrMultipleArguments() {
        // Three arguments - all true
        test(true, """
                or:
                  - true
                  - true
                  - true
                """);

        // Three arguments - one true at start
        test(true, """
                or:
                  - true
                  - false
                  - false
                """);

        // Three arguments - one true in middle
        test(true, """
                or:
                  - false
                  - true
                  - false
                """);

        // Three arguments - one true at end
        test(true, """
                or:
                  - false
                  - false
                  - true
                """);
        // Three arguments - all false
        test(false, """
                or:
                  - false
                  - false
                  - false
                """);
        // Four arguments - all true
        test(true, """
                or:
                  - true
                  - true
                  - true
                  - true
                """);
        // Four arguments - one true
        test(true, """
                or:
                  - false
                  - false
                  - true
                  - false
                """);
        // Four arguments - all false
        test(false, """
                or:
                  - false
                  - false
                  - false
                  - false
                """);
    }

    @Test
    void testOrWithTruthyFalsyValues() {
        // Truthy values
        testFail("""
                or:
                  - 0
                  - 1
                """);
        testFail("""
                or:
                  - null
                  - "text"
                """);
        // All falsy values
        testFail("""
                or:
                  - 0
                  - null
                """);
        testFail("""
                or:
                  - false
                  - 0
                """);
    }

    @Test
    void testOrInvalidArguments() {
        // No arguments
        testFail("""
                or: []
                """);

        // Single argument
        testFail("""
                or:
                  - true
                """);
    }

    @Test
    void testComplexLogicalExpressions() {
        // NOT applied to AND result
        test(false, """
                "!":
                  - and:
                      - true
                      - true
                """);

        test(true, """
                "!":
                  - and:
                      - true
                      - false
                """);

        // AND with comparisons
        test(true, """
                and:
                  - "<":
                      - 1
                      - 5
                  - "<":
                      - 3
                      - 7
                """);

        // OR with comparisons
        test(true, """
                or:
                  - "<":
                      - 5
                      - 3
                  - "<":
                      - 3
                      - 5
                """);

        // Mixed logical operators
        test(true, """
                or:
                  - "!":
                      - true
                  - and:
                      - true
                      - true
                """);
    }

}

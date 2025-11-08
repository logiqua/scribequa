package com.javax0.logiqua.xml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestXmlLogiquaBoolAndComparator {


    private void test(final Object result, final String script) {
        final var scriptObject = new XmlLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    private void testFail(final String script) {
        Assertions.assertThrows(Exception.class, () -> {
            final var scriptObject = new XmlLogiqua().compile(script);
            scriptObject.evaluate();
        });
    }

    @Test
    void testEquals() {
        test(false, """
                <op symbol="==">
                    <constant integer="3"/>
                    <constant integer="5"/>
                </op>
                """);
    }


    @Test
    void testLessThan() {
        // Two arguments - true case
        test(true, """
                <op symbol="&lt;">
                    <constant integer="3"/>
                    <constant integer="5"/>
                </op>
                """);

        // Two arguments - false case
        test(false, """
                <op symbol="&lt;">
                    <constant integer="5"/>
                    <constant integer="3"/>
                </op>
                """);

        // Two arguments - equal (false)
        test(false, """
                <op symbol="&lt;">
                    <constant integer="5"/>
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testLessThanMultipleArguments() {
        // Three arguments - all true
        test(true, """
                <op symbol="&lt;">
                    <constant integer="1"/>
                    <constant integer="3"/>
                    <constant integer="5"/>
                </op>
                """);

        // Three arguments - middle comparison fails
        test(false, """
                <op symbol="&lt;">
                    <constant integer="1"/>
                    <constant integer="5"/>
                    <constant integer="3"/>
                </op>
                """);

        // Four arguments - all true
        test(true, """
                <op symbol="&lt;">
                    <constant integer="1"/>
                    <constant integer="2"/>
                    <constant integer="3"/>
                    <constant integer="4"/>
                </op>
                """);
    }

    @Test
    void testLessThanInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="&lt;">
                </op>
                """);

        // Single argument
        testFail("""
                <op symbol="&lt;">
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testGreaterThan() {
        // Two arguments - true case
        test(true, """
                <op symbol="&gt;">
                    <constant integer="5"/>
                    <constant integer="3"/>
                </op>
                """);

        // Two arguments - false case
        test(false, """
                <op symbol="&gt;">
                    <constant integer="3"/>
                    <constant integer="5"/>
                </op>
                """);

        // Two arguments - equal (false)
        test(false, """
                <op symbol="&gt;">
                    <constant integer="5"/>
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testGreaterThanMultipleArguments() {
        // Three arguments - all true
        test(true, """
                <op symbol="&gt;">
                    <constant integer="5"/>
                    <constant integer="3"/>
                    <constant integer="1"/>
                </op>
                """);

        // Three arguments - middle comparison fails
        test(false, """
                <op symbol="&gt;">
                    <constant integer="5"/>
                    <constant integer="1"/>
                    <constant integer="3"/>
                </op>
                """);

        // Four arguments - all true
        test(true, """
                <op symbol="&gt;">
                    <constant integer="10"/>
                    <constant integer="8"/>
                    <constant integer="6"/>
                    <constant integer="4"/>
                </op>
                """);
    }

    @Test
    void testGreaterThanInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="&gt;">
                </op>
                """);

        // Single argument
        testFail("""
                <op symbol="&gt;">
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testNotEqual() {
        // Two arguments - true case
        test(true, """
                <op symbol="!=">
                    <constant integer="3"/>
                    <constant integer="5"/>
                </op>
                """);

        // Two arguments - false case (equal)
        test(false, """
                <op symbol="!=">
                    <constant integer="5"/>
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testNotEqualMultipleArguments() {
        // Three arguments - all different
        test(true, """
                <op symbol="!=">
                    <constant integer="1"/>
                    <constant integer="2"/>
                    <constant integer="3"/>
                </op>
                """);

        // Three arguments - two equal
        test(false, """
                <op symbol="!=">
                    <constant integer="1"/>
                    <constant integer="2"/>
                    <constant integer="1"/>
                </op>
                """);

        // Four arguments - all different
        test(true, """
                <op symbol="!=">
                    <constant integer="1"/>
                    <constant integer="2"/>
                    <constant integer="3"/>
                    <constant integer="4"/>
                </op>
                """);

        // Four arguments - one duplicate
        test(true, """
                <op symbol="!=">
                    <constant integer="1"/>
                    <constant integer="2"/>
                    <constant integer="3"/>
                    <constant integer="2"/>
                </op>
                """);
    }

    @Test
    void testNotEqualInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="!=">
                </op>
                """);

        // Single argument
        testFail("""
                <op symbol="!=">
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testLessThanOrEqual() {
        // Two arguments - true case (less than)
        test(true, """
                <op symbol="&lt;=">
                    <constant integer="3"/>
                    <constant integer="5"/>
                </op>
                """);

        // Two arguments - true case (equal)
        test(true, """
                <op symbol="&lt;=">
                    <constant integer="5"/>
                    <constant integer="5"/>
                </op>
                """);

        // Two arguments - false case
        test(false, """
                <op symbol="&lt;=">
                    <constant integer="5"/>
                    <constant integer="3"/>
                </op>
                """);
    }

    @Test
    void testLessThanOrEqualMultipleArguments() {
        // Three arguments - all true (ascending)
        test(true, """
                <op symbol="&lt;=">
                    <constant integer="1"/>
                    <constant integer="3"/>
                    <constant integer="5"/>
                </op>
                """);

        // Three arguments - with equality
        test(true, """
                <op symbol="&lt;=">
                    <constant integer="1"/>
                    <constant integer="1"/>
                    <constant integer="5"/>
                </op>
                """);

        // Three arguments - fails
        test(false, """
                <op symbol="&lt;=">
                    <constant integer="1"/>
                    <constant integer="5"/>
                    <constant integer="3"/>
                </op>
                """);

        // Four arguments - all equal
        test(true, """
                <op symbol="&lt;=">
                    <constant integer="5"/>
                    <constant integer="5"/>
                    <constant integer="5"/>
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testLessThanOrEqualInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="&lt;=">
                </op>
                """);

        // Single argument
        testFail("""
                <op symbol="&lt;=">
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testGreaterThanOrEqual() {
        // Two arguments - true case (greater than)
        test(true, """
                <op symbol=">=">
                    <constant integer="5"/>
                    <constant integer="3"/>
                </op>
                """);

        // Two arguments - true case with >=
        test(true, """
                <op symbol=">=">
                    <constant integer="5"/>
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testGreaterThanOrEqualMultipleArguments() {
        // Three arguments - all true (descending)
        test(true, """
                <op symbol=">=">
                    <constant integer="5"/>
                    <constant integer="3"/>
                    <constant integer="1"/>
                </op>
                """);

        // Three arguments - with equality
        test(true, """
                <op symbol=">=">
                    <constant integer="5"/>
                    <constant integer="5"/>
                    <constant integer="1"/>
                </op>
                """);

        // Three arguments - fails
        test(false, """
                <op symbol=">=">
                    <constant integer="5"/>
                    <constant integer="1"/>
                    <constant integer="3"/>
                </op>
                """);

        // Four arguments - all equal
        test(true, """
                <op symbol=">=">
                    <constant integer="5"/>
                    <constant integer="5"/>
                    <constant integer="5"/>
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testGreaterThanOrEqualInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="=">
                </op>
                """);

        // Single argument
        testFail("""
                <op symbol="=">
                    <constant integer="5"/>
                </op>
                """);
    }

    @Test
    void testNot() {
        // Single argument - true case
        test(false, """
                <op symbol="!">
                    <true/>
                </op>
                """);

        // Single argument - false case
        test(true, """
                <op symbol="!">
                    <false/>
                </op>
                """);
        test(false, """
                <op symbol="!">
                    <true/>
                </op>
                """);

        // Single argument - false case
        test(true, """
                <op symbol="!">
                    <false/>
                </op>
                """);

        // Single argument - truthy value
        testFail("""
                <op symbol="!">
                    <constant integer="1"/>
                </op>
                """);

        // Single argument - falsy value
        testFail("""
                <op symbol="!">
                    <constant integer="0"/>
                </op>
                """);

        // Single argument - null
        testFail("""
                <op symbol="!">
                    <null/>
                </op>
                """);
    }

    @Test
    void testNotInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="!">
                </op>
                """);

        // Multiple arguments
        testFail("""
                <op symbol="!">
                    <true/>
                    <false/>
                </op>
                """);
    }

    @Test
    void testAnd() {
        // Two arguments - both true
        test(true, """
                <op symbol="and">
                    <true/>
                    <true/>
                </op>
                """);

        // Two arguments - first false
        test(false, """
                <op symbol="and">
                    <false/>
                    <true/>
                </op>
                """);

        // Two arguments - second false
        test(false, """
                <op symbol="and">
                    <true/>
                    <false/>
                </op>
                """);

        // Two arguments - both false
        test(false, """
                <op symbol="and">
                    <false/>
                    <false/>
                </op>
                """);
    }

    @Test
    void testAndMultipleArguments() {
        // Three arguments - all true
        test(true, """
                <op symbol="and">
                    <true/>
                    <true/>
                    <true/>
                </op>
                """);

        // Three arguments - one false at start
        test(false, """
                <op symbol="and">
                    <false/>
                    <true/>
                    <true/>
                </op>
                """);

        // Three arguments - one false in middle
        test(false, """
                <op symbol="and">
                    <true/>
                    <false/>
                    <true/>
                </op>
                """);

        // Three arguments - one false at end
        test(false, """
                <op symbol="and">
                    <true/>
                    <true/>
                    <false/>
                </op>
                """);

        // Four arguments - all true
        test(true, """
                <op symbol="and">
                    <true/>
                    <true/>
                    <true/>
                    <true/>
                </op>
                """);

        // Four arguments - one false
        test(false, """
                <op symbol="and">
                    <true/>
                    <true/>
                    <false/>
                    <true/>
                </op>
                """);
    }

    @Test
    void testAndWithTruthyFalsyValues() {
        // Truthy and falsy values
        testFail("""
                <op symbol="and">
                    <constant integer="1"/>
                    <constant integer="2"/>
                    <constant integer="3"/>
                </op>
                """);
        testFail("""
                <op symbol="and">
                    <constant integer="1"/>
                    <constant integer="0"/>
                    <constant integer="3"/>
                </op>
                """);
        testFail("""
                <op symbol="and">
                    <constant integer="1"/>
                    <null/>
                    <constant integer="3"/>
                </op>
                """);
    }

    @Test
    void testAndInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="and">
                </op>
                """);

        // Single argument
        testFail("""
                <op symbol="and">
                    <true/>
                </op>
                """);
    }

    @Test
    void testOr() {
        // Two arguments - both true
        test(true, """
                <op symbol="or">
                    <true/>
                    <true/>
                </op>
                """);

        // Two arguments - first true
        test(true, """
                <op symbol="or">
                    <true/>
                    <false/>
                </op>
                """);

        // Two arguments - second true
        test(true, """
                <op symbol="or">
                    <false/>
                    <true/>
                </op>
                """);

        // Two arguments - both false
        test(false, """
                <op symbol="or">
                    <false/>
                    <false/>
                </op>
                """);
    }

    @Test
    void testOrMultipleArguments() {
        // Three arguments - all true
        test(true, """
                <op symbol="or">
                    <true/>
                    <true/>
                    <true/>
                </op>
                """);

        // Three arguments - one true at start
        test(true, """
                <op symbol="or">
                    <true/>
                    <false/>
                    <false/>
                </op>
                """);

        // Three arguments - one true in middle
        test(true, """
                <op symbol="or">
                    <false/>
                    <true/>
                    <false/>
                </op>
                """);

        // Three arguments - one true at end
        test(true, """
                <op symbol="or">
                    <false/>
                    <false/>
                    <true/>
                </op>
                """);
        // Three arguments - all false
        test(false, """
                <op symbol="or">
                    <false/>
                    <false/>
                    <false/>
                </op>
                """);
        // Four arguments - all true
        test(true, """
                <op symbol="or">
                    <true/>
                    <true/>
                    <true/>
                    <true/>
                </op>
                """);
        // Four arguments - one true
        test(true, """
                <op symbol="or">
                    <false/>
                    <false/>
                    <true/>
                    <false/>
                </op>
                """);
        // Four arguments - all false
        test(false, """
                <op symbol="or">
                    <false/>
                    <false/>
                    <false/>
                    <false/>
                </op>
                """);
    }

    @Test
    void testOrWithTruthyFalsyValues() {
        // Truthy values
        testFail("""
                <op symbol="or">
                    <constant integer="0"/>
                    <constant integer="1"/>
                </op>
                """);
        testFail("""
                <op symbol="or">
                    <null/>
                    <constant string="text"/>
                </op>
                """);
        // All falsy values
        testFail("""
                <op symbol="or">
                    <constant integer="0"/>
                    <null/>
                </op>
                """);
        testFail("""
                <op symbol="or">
                    <false/>
                    <constant integer="0"/>
                </op>
                """);
    }

    @Test
    void testOrInvalidArguments() {
        // No arguments
        testFail("""
                <op symbol="or">
                </op>
                """);

        // Single argument
        testFail("""
                <op symbol="or">
                    <true/>
                </op>
                """);
    }

    @Test
    void testComplexLogicalExpressions() {
        // NOT applied to AND result
        test(false, """
                <op symbol="!">
                    <op symbol="and">
                        <true/>
                        <true/>
                    </op>
                </op>
                """);

        test(true, """
                <op symbol="!">
                    <op symbol="and">
                        <true/>
                        <false/>
                    </op>
                </op>
                """);

        // AND with comparisons
        test(true, """
                <op symbol="and">
                    <op symbol="&lt;">
                        <constant integer="1"/>
                        <constant integer="5"/>
                    </op>
                    <op symbol="&lt;">
                        <constant integer="3"/>
                        <constant integer="7"/>
                    </op>
                </op>
                """);

        // OR with comparisons
        test(true, """
                <op symbol="or">
                    <op symbol="&lt;">
                        <constant integer="5"/>
                        <constant integer="3"/>
                    </op>
                    <op symbol="&lt;">
                        <constant integer="3"/>
                        <constant integer="5"/>
                    </op>
                </op>
                """);

        // Mixed logical operators
        test(true, """
                <op symbol="or">
                    <op symbol="!">
                        <true/>
                    </op>
                    <op symbol="and">
                        <true/>
                        <true/>
                    </op>
                </op>
                """);
    }

}

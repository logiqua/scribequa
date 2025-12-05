package com.javax0.logiqua.exp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestExpLogiquaBoolAndComparator {


    private void test(final Object result, final String script) {
        final var scriptObject = new ExpLogiqua().compile(script);
        Assertions.assertEquals(result, scriptObject.evaluate());
    }

    private void testFail(final String script) {
        Assertions.assertThrows(Exception.class, () -> {
            final var scriptObject = new ExpLogiqua().compile(script);
            scriptObject.evaluate();
        });
    }

    @Test
    void testEquals() {
        test(false, "3 == 5");
    }


    @Test
    void testLessThan() {
        // Two arguments - true case
        test(true, "3 <5");

        // Two arguments - false case
        test(false, "5< 3");

        // Two arguments - equal (false)
        test(false, "5 < 5 ");
    }

    @Test
    void testLessThanMultipleArguments() {
        // Three arguments - all true
        test(true, " 1 < 3 and 3 < 5");

        // Three arguments - middle comparison fails
        test(false, "1<5 and 5<3");

        // Four arguments - all true
        test(true, "1<2 and 1< 3 and 1< 4");
    }

    @Test
    void testGreaterThan() {
        // Two arguments - true case
        test(true, "5 > 3");

        // Two arguments - false case
        test(false, "3 > 5");

        // Two arguments - equal (false)
        test(false, "5 > 5");
    }

    @Test
    void testNotEqual() {
        // Two arguments - true case
        test(true, "3 != 5");

        // Two arguments - false case (equal)
        test(false, "5 != 5");
    }

    @Test
    void testLessThanOrEqual() {
        // Two arguments - true case (less than)
        test(true, "3 <= 5");

        // Two arguments - true case (equal)
        test(true, "5 <= 5");

        // Two arguments - false case
        test(false, "5 <=3");
    }

    @Test
    void testGreaterThanOrEqual() {
        // Two arguments - true case (greater than)
        test(true, "5 >= 3");

        // Two arguments - true case with >=
        test(true, "5 >= 5");
    }

    @Test
    void testNot() {
        // Single argument - true case
        test(false, "! true");

        // Single argument - false case
        test(true, "! false");

        // Single argument - truthy value
        testFail("! 1 ");

        // Single argument - falsy value
        testFail("! 0 ");

        // Single argument - null
        testFail("! null ");
    }

    @Test
    void testNotInvalidArguments() {
        // No arguments
        testFail("! []");

        // Multiple arguments
        testFail("! [true false]");
    }

    @Test
    void testAnd() {
        // Two arguments - both true
        test(true, "true and true");

        // Two arguments - first false
        test(false, "false and true");

        // Two arguments - second false
        test(false, "true and false");

        // Two arguments - both false
        test(false, "false and false");
    }

    @Test
    void testAndMultipleArguments() {
        // Three arguments - all true
        test(true, "true and true and true");

        // Three arguments - one false at start
        test(false, "false and true and true");

        // Three arguments - one false in middle
        test(false, "true and false and true");

        // Three arguments - one false at end
        test(false, "true and true and false");

        // Four arguments - all true
        test(true, "true and true and true and true");

        // Four arguments - one false
        test(false, "true and true and false and true");
    }

    @Test
    void testAndWithTruthyFalsyValues() {
        // Truthy and falsy values
        testFail("1 and 2 and 3");
        testFail("1 and 0 and 3");
        testFail("1 and null and 3");
    }

    @Test
    void testAndInvalidArguments() {
        // No arguments
        testFail("and");

        // Single argument
        testFail("and true");
    }

    @Test
    void testOr() {
        // Two arguments - both true
        test(true, "true or true");

        // Two arguments - first true
        test(true, "true or false");

        // Two arguments - second true
        test(true, "false or true");

        // Two arguments - both false
        test(false, "false or false");
    }

    @Test
    void testOrMultipleArguments() {
        // Three arguments - all true
        test(true, "true or true or true");

        // Three arguments - one true at start
        test(true, "true or false or false");

        // Three arguments - one true in middle
        test(true, "false or true or false");

        // Three arguments - one true at end
        test(true, "false or false or true");
        // Three arguments - all false
        test(false, "false or false or false");
        // Four arguments - all true
        test(true, "true or true or true or true");
        // Four arguments - one true
        test(true, "false or false or true or false");
        // Four arguments - all false
        test(false, "false or false or false or false");
    }

    @Test
    void testOrWithTruthyFalsyValues() {
        // Truthy values
        testFail("0 or 1");
        testFail("null or text");
        // All falsy values
        testFail("0 or null");
        testFail("false or 0");
    }

    @Test
    void testOrInvalidArguments() {
        // No arguments
        testFail("or");

        // Single argument
        testFail("or true");
    }

    @Test
    void testComplexLogicalExpressions() {
        // Mixed logical operators
        test(true, "(! true) or (true and true)");

        // NOT applied to AND result
        test(false, "! (true and true)");

        test(true, "! (true and false)");

        // AND with comparisons
        test(true, "1 < 5 and 3 < 7");

        // OR with comparisons
        test(true, "5 < 3 or 3 < 5");

    }

}

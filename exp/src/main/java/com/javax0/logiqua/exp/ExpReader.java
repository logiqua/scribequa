package com.javax0.logiqua.exp;

import com.javax0.lex.Token;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The ExpReader class is responsible for parsing and evaluating tokens that represent
 * expressions. It supports operations with varying levels of precedence and handles
 * various types of tokens such as operators, identifiers, constants, and functions.
 * <p>
 * Static constants and methods are also defined to help process binary operations.
 */
public class ExpReader {
    static final String[][] binaryOperators = {
            {"or"},                             // 0
            {"and"},                            // 1
            {"!==", "===", "==", "!=", "in",
                    "all", "none", "map", "some",
                    "missing", "missing_some"}, // 2
            {"<", "<=", ">", ">="},             // 3
            {"+", "-"},                         // 4
            {"*", "/", "%"}                     // 5
    };

    final TokenIterator tokens;

    public static ExpReader of(final TokenIterator elements) {
        return new ExpReader(elements);
    }

    private ExpReader(final TokenIterator elements) {
        this.tokens = elements;
    }


    /**
     * Reads the next expression from the token stream. This method analyzes the token stream and constructs
     * an appropriate representation of the expression. If the expression starts with the "!" symbol, it
     * parses it as a special case, creating a composite structure consisting of the "!" token followed by
     * the further parsed expression. Otherwise, it delegates to a sub-expression reader based on priority.
     *
     * @return the parsed representation of the next expression from the token stream, which can be a composite
     * object, a list representing expressions, or a result based on priority-handled sub-expressions.
     */
    public Object read() {
        if (!tokens.eof() && tokens.current().is("!")) {
            final var token = tokens.next();
            final var ret = new ArrayList<>();
            ret.add(token);
            ret.add(read());
            return ret;
        }
        return readSubExpression(0);
    }

    private Object readSubExpression(int priority) {
        if (priority == binaryOperators.length) {
            return readTerminal(priority);
        } else {
            var left = readSubExpression(priority + 1);
            while (isOperator(priority)) {
                final var operator = tokens.next();
                final var right = readSubExpression(priority + 1);
                left = List.of(operator, left, right);
            }
            return left;
        }
    }

    private Object readTerminal(int priority) {
        final var token = tokens.next();
        if (token.is("(")) {
            final var expression = read();
            if (tokens.eof() || !tokens.current().is(")")) {
                throw new IllegalArgumentException("The expression started with '(' must end with ')'");
            }
            tokens.next();
            return expression;
        }

        if (token.is("[")) {
            final var list = new ArrayList<>();
            if (!tokens.current().is("]")) {
                addExpressionList(list);
            }
            if (!tokens.current().is("]")) {
                throw new IllegalArgumentException("The list must end with ']' before EOF");
            }
            tokens.next();
            return list;
        }

        if (token.is("+") || token.is("-")) {
            final var ret = new ArrayList<>();
            ret.add(token);
            ret.add(readSubExpression(priority));
            return ret;
        }
        if (token.is(Keyword.class)) {
            return switch (token.lexeme()) {
                case "true", "false", "null" -> token.value();
                default ->
                        throw new IllegalArgumentException("JSON cannot handle the keyword '" + token.lexeme() + "'");
            };
        }
        if (token.isOneOf(StringToken.class, IntegerNumber.class, FloatingNumber.class, Constant.class)) {
            return token.value();
        }
        if (canBeIdentifier(token)) {
            if (!tokens.eof() && tokens.current().is("(")) {
                tokens.next();
                final var function = new ArrayList<>();
                function.add(token);
                if (!tokens.eof() && !tokens.current().is(")")) {
                    addExpressionList(function);
                }
                if (tokens.eof() || !tokens.current().is(")")) {
                    throw new IllegalArgumentException("The function call must end with ')'");
                }
                tokens.next();
                return function;
            } else {
                return token;
            }
        }
        if (token.isOneOf(Symbol.class)) {
            return token;
        }
        throw new IllegalArgumentException("Cannot read the value from the token " + token);
    }

    private static boolean canBeIdentifier(Token<?> token) {
        return token.isOneOf(Identifier.class, Symbol.class) && Character.isJavaIdentifierStart(token.lexeme().charAt(0));
    }

    private void addExpressionList(final List<Object> function) {
        while (!tokens.eof()) {
            function.add(read());
            if (!tokens.eof() && tokens.current().is(",")) {
                tokens.next();
            } else {
                return;
            }
        }
    }

    private boolean isOperator(int priority) {
        if (tokens.eof()) {
            return false;
        }
        for (final var op : binaryOperators[priority]) {
            if (tokens.current().is(op)) {
                return true;
            }
        }
        return false;
    }

}

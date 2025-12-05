package com.javax0.logiqua.exp;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.tokens.IntegerNumber;

/**
 * Analyzer for integer numbers. This analyser does not handle the '+' or '-' in front of the number.
 * In the infix notation (normal expressions as we know them) the '+' or '-' is handled as unary operators
 * or as binary operators.
 */
public class ExpIntegerNumberAnalyzer implements Analyzer<Long> {

    @Override
    public IntegerNumber analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        final var sb = new StringBuilder();
        while (!input.eof() && Character.isDigit(input.peek())) {
            sb.append(input.next());
        }
        if (sb.isEmpty()) {
            return null;
        }
        return new IntegerNumber(start, input.position(), Long.parseLong(sb.toString()), sb.toString());
    }
}

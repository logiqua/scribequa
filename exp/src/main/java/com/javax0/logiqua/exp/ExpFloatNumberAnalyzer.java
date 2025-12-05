package com.javax0.logiqua.exp;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.tokens.FloatingNumber;

/**
 * Analyzer for float numbers. This analyser does not handle the '+' or '-' in front of the number.
 * In the infix notation (normal expressions as we know them) the '+' or '-' is handled as unary operators
 * or as binary operators.
 */
public class ExpFloatNumberAnalyzer implements Analyzer<Double> {

    @Override
    public FloatingNumber analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        final var sb = new StringBuilder();

        while (!input.eof() && Character.isDigit(input.peek())) {
            sb.append(input.next());
        }
        if (input.startsWith(".")) {
            sb.append(".");
            input.next();
            while (!input.eof() && Character.isDigit(input.peek())) {
                sb.append(input.next());
            }
            if (input.startsWith("e") || input.startsWith("E")) {
                sb.append("e");
                input.next();
                if (input.startsWith("+") || input.startsWith("-")) {
                    sb.append(input.next());
                }
                while (!input.eof() && Character.isDigit(input.peek())) {
                    sb.append(input.next());
                }
            }
        }
        if (sb.isEmpty()) {
            return null;
        }
        final var value = Double.parseDouble(sb.toString());
        return new FloatingNumber(start, input.position(), value, sb.toString());
    }
}

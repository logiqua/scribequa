package com.javax0.lex.analyzers;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.tokens.IntegerNumber;

public class IntegerNumberAnalyzer implements Analyzer<Long> {

    @Override
    public IntegerNumber analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        final var sb = new StringBuilder();
        if( input.startsWith("-") || input.startsWith("+") ){
            sb.append(input.next());
        }
        while (!input.eof() && Character.isDigit(input.peek())) {
            sb.append(input.next());
        }
        if (sb.isEmpty()) {
            return null;
        }
        return new IntegerNumber(start, input.position(), Long.parseLong(sb.toString()), sb.toString());
    }
}

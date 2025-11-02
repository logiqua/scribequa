package com.javax0.lex.analyzers;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.tokens.Space;

public class SpaceAnalyzer implements Analyzer<String> {
    @Override
    public Space analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        Input.Index index = input.index();
        char ch = input.next();
        final var sb = new StringBuilder();
        while (Character.isWhitespace(ch) && ch != '\n') {
            sb.append(ch);
            index = input.index();
            if(input.eof()) {
                break;
            }
            ch = input.next();
        }

        input.reset(index);
        final var end = input.position();
        if (sb.isEmpty()) {
            return null;
        }
        return new Space(start, end, sb.toString(), sb.toString());
    }
}

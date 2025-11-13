package com.javax0.logiqua.lsp;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.Position;
import com.javax0.lex.Token;

public class LispCommentAnalyzer implements Analyzer<String> {

    record Comment(Position start, Position end, String value, String lexeme) implements Token<String>{
    }

    @Override
    public Token<String> analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        final var ch = input.peek();
        if (ch != ';') {
            return null;
        }
        final var sb = new StringBuilder();
        while (!input.eof() && input.peek() != '\n') {
            sb.append(input.next());
        }
        final var end = input.position();
        return new Comment(start, end, sb.toString(), sb.toString());
    }
}

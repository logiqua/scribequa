package com.javax0.logiqua.exp;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.Token;
import com.javax0.lex.tokens.Symbol;

public class ExpSymbolAnalyzer implements Analyzer<String> {

    @Override
    public Token<String> analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        final var ch = input.peek();
        if (ch == '"') {
            return null;
        }

        for (final var keyword : new String[]{"or", "and", "in"}) {
            if (input.length() > keyword.length() && input.startsWith(keyword) && !Character.isAlphabetic(input.peek(keyword.length()))) {
                for (int i = 0; i < keyword.length(); i++) {
                    input.next();
                }
                return new Symbol(start, input.position(), keyword, keyword);
            }
        }


        for (final var symbolText : new String[]{"!=", "==", "===", "<=", ">="}) {
            if (input.length() >= symbolText.length() && input.startsWith(symbolText)) {
                for (int i = 0; i < symbolText.length(); i++) {
                    input.next();
                }
                return new Symbol(start, input.position(), symbolText, symbolText);
            }
        }

        if (Character.isAlphabetic(input.peek()) || Character.isDigit(input.peek()) || Character.isWhitespace(input.peek())) {
            return null;
        }

        // any one character symbol
        final var symbol = "" + input.next();
        final var end = input.position();
        return new Symbol(start, end, symbol, symbol);
    }
}

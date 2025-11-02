package com.javax0.lex.analyzers;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.Token;
import com.javax0.lex.tokens.Symbol;

/**
 * SymbolAnalyzer is a lexical analyzer that identifies and processes specific
 * predefined symbols from an input source. It checks whether the input starts
 * with any of the predefined symbols and, if so, extracts that symbol as a
 * lexical element.
 *
 * The symbols to be recognized can be customized by providing a different
 * set of symbols through the {@code setSymbols} method.
 *
 * This analyzer is typically used in lexical analysis to parse and identify symbol
 * tokens, such as punctuation or special characters.
 */
public class SymbolAnalyzer implements Analyzer<String> {

    private String[] symbols = new String[]{"{", "}", "[", "]", ":", ","};

    public void setSymbols(String[] symbols) {
        this.symbols = symbols;
    }

    @Override
    public Token<String> analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        var index = input.index();
        for (var symbol : symbols) {
            if (input.startsWith(symbol)) {
                for (int i = 0; i < symbol.length() - 1; i++) {
                    input.next();
                }
                input.next();
                final var end = input.position();
                return new Symbol(start, end, symbol, symbol);
            }
        }
        return null;
    }
}

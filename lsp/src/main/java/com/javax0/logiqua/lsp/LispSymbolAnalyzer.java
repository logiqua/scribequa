package com.javax0.logiqua.lsp;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.Token;
import com.javax0.lex.tokens.Symbol;

/**
 * A greedy symbol analyser accepts just anything that starts with something that is not a letter or number,
 * and lasts until the next space, letter, or digit. It has a predefined list of symbols that it accepts, but if it
 * cannot find a symbol in the predefined list, it accepts anything that starts with a non-letter / non-digit.
 * <p>
 * This analyser is not listed in {@code META-INF/services/com.javax0.lex.Analyzer} file, as it is used as a replacement
 * for the {@code SymbolAnalyzer} when the predefined list of symbols is not needed.
 * <p>
 * The predefined set o symbols contain '{@code (}', and '{@code )}' to accommodate the default that the lisp analyzer
 * needs.
 */
public class LispSymbolAnalyzer implements Analyzer<String> {

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
        if (ch == '(' || ch == ')') {
            final var symbol = "" + input.next();
            final var end = input.position();
            return new Symbol(start, end, symbol, symbol);
        }
        if (Character.isLetterOrDigit(input.peek())) {
            return null;
        }
        final var sb = new StringBuilder();
        while (!input.eof() && !Character.isWhitespace(input.peek())
                && !Character.isLetterOrDigit(input.peek())
                && input.peek() != '(' && input.peek() != ')') {
            sb.append(input.next());
        }
        final var end = input.position();
        return new Symbol(start, end, sb.toString(), sb.toString());
    }
}

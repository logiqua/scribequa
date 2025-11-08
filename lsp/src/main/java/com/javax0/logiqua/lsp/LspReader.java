package com.javax0.logiqua.lsp;

import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.*;

import java.util.ArrayList;
import java.util.List;

public class LspReader {
    TokenIterator tokens;

    public static LspReader of(final TokenIterator elements) {
        return new LspReader(elements);
    }

    private LspReader(final TokenIterator elements) {
        this.tokens = elements;
    }


    public Object read() {
        if (tokens.current().is("(")) {
            final var result = readArray();
            return result;
        }
        final var token = tokens.next();
        if (token.is(Keyword.class)) {
            return switch (token.lexeme()) {
                case "true" -> token.value();
                case "false" -> token.value();
                case "null" -> token.value();
                default ->
                        throw new IllegalArgumentException("JSON cannot handle the keyword '" + token.lexeme() + "'");
            };
        }
        if (token.isOneOf(StringToken.class, IntegerNumber.class, FloatingNumber.class, Constant.class)) {
            return token.value();
        }
        if(token.isOneOf(Identifier.class, Symbol.class)){
            return token;
        }
        throw new IllegalArgumentException("Cannot read the value from the token " + token);
    }

    List<Object> readArray() {
        final var list = new ArrayList<Object>();
        if (tokens.current().is("(")) {
            tokens.next();
            if (tokens.current().is(")")) {
                tokens.next();
                return List.of();
            }
            while (!tokens.eof()) {
                final var value = read();
                list.add(value);
                if (!tokens.eof()) {
                    if (tokens.current().is(")")) {
                        break;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("A LSP list  must start with '('");
        }

        if (!tokens.eof() && tokens.current().is(")")) {
            tokens.next(); // step over the closing ')'
        } else {
            throw new IllegalArgumentException("The LSP list must end with ')' before EOF");
        }
        return list;
    }
}

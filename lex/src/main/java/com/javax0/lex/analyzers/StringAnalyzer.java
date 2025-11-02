package com.javax0.lex.analyzers;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.tokens.StringToken;

public class StringAnalyzer implements Analyzer<String> {


    private static final String ESCAPES = "btnfr\"'\\`";
    private static final String ESCAPED = "\b\t\n\f\r\"'\\`";


    @Override
    public StringToken analyse(Input input) {
        if (input.startsWith("\"")) {
            final var start = input.position();
            final var text = new StringBuilder();
            final var lexeme = new StringBuilder();
            lexeme.append(input.next());
            while (!input.eof() && !input.startsWith("\"")) {
                final char ch = input.next();
                if (ch == '\\') {
                    if (input.eof()) {
                        return null;
                    }
                    lexeme.append(ch);
                    final var esch = input.next();
                    final int esindex = ESCAPES.indexOf(esch);
                    if (esindex == -1) {
                        if (esch >= '0' && esch <= '3') {
                            text.append(octal(input, 3, lexeme));
                        } else if (esch >= '4' && esch <= '7') {
                            text.append(octal(input, 2, lexeme));
                        } else if (esch == 'u') {
                            lexeme.append(esch);
                            text.append(hex(input, 4, lexeme));
                        } else {
                            return null;
                        }
                    } else {
                        text.append(ESCAPED.charAt(esindex));
                        lexeme.append(esch);
                    }
                } else {
                    if (ch == '\n' || ch == '\r') {
                        return null;
                    }
                    lexeme.append(ch);
                    text.append(ch);
                }
            }
            if (input.startsWith("\"")) {
                final var end = input.position();
                lexeme.append(input.next());
                return new StringToken(start, end, text.toString(), lexeme.toString());
            }
        }
        return null;
    }


    private static char octal(Input in, int maxLen, StringBuilder lexeme) {
        int i = maxLen;
        int occ = 0;
        var index = in.index();
        var ch = in.next();
        while (i > 0 && !in.eof() && ch >= '0' && ch <= '7') {
            occ = 8 * occ + ch - '0';
            lexeme.append(ch);
            index = in.index();
            ch = in.next();
            i--;
        }
        in.reset(index);
        return (char) occ;
    }

    private static char hex(final Input in, final int maxLen, final StringBuilder lexeme) {
        int i = maxLen;
        int hx = 0;
        var index = in.index();
        var ch = in.next();
        while (i > 0 && !in.eof() && ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))) {
            lexeme.append(ch);
            hx = 16 * hx;
            if (ch <= '9') {
                hx += ch - '0';
            } else if (ch <= 'F') {
                hx += ch - 'A' + 10;
            } else {
                hx += ch - 'a' + 10;
            }
            index = in.index();
            ch = in.next();
            i--;
        }
        in.reset(index);
        return (char) hx;
    }
}

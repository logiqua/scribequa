package com.javax0.lex.analyzers;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.tokens.NewLine;

/**
 * NewLineAnalyzer is a lexical analyzer that identifies and processes a single newline
 * characters from an input source. It specifically checks if the input starts
 * with a newline character ("\n") and, if so, creates a corresponding NewLine
 * lexical element.
 * <p>
 * This analyzer returns a {@code NewLine} element with its start and end positions,
 * as well as the value and lexeme of the newline character, allowing the lexer
 * to recognize and handle newline tokens during parsing.
 * <p>
 * If the input does not start with a newline character or if the input has reached
 * the end, the analyzer returns {@code null}, signifying that no newline token
 * was identified.
 */
public class NewLineAnalyzer implements Analyzer<String> {
    @Override
    public NewLine analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        if (input.startsWith("\n")) {
            input.next();
            return new NewLine(start, input.position(), "\n", "\n");
        } else {
            return null;
        }
    }
}

package com.javax0.lex.analyzers;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.Position;
import com.javax0.lex.Token;
import com.javax0.lex.tokens.Constant;
import com.javax0.lex.tokens.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The IdentifierAnalyzer class is responsible for analyzing input text and identifying
 * valid identifiers. An identifier is a sequence of characters that starts with
 * a valid identifier start character, and is followed by zero or more valid Java
 * identifier part characters. This class distinguishes between keywords (e.g., "true",
 * "false", "null") and general identifiers.
 * <p>
 * Implements the Analyzer interface for processing input and returning tokens of type String.
 * <p>
 * Core functionality:
 * <ul>
 *   - Identifies valid identifiers based on character rules defined in the Java language.
 *   - Distinguishes special keywords (like "true", "false", "null") from general identifiers.
 *   - Provides a mechanism to customize or override the set of keywords.
 * </ul>
 * <p>
 * Key behaviors:
 * - When a valid identifier is found, it returns a token of type {@link Identifier} or
 *   {@link Constant}, depending on whether the identifier is in the keyword set.
 * - If the current input does not match the identifier rules, no token is returned.
 * - The analysis resumes from the input's current position after processing, resetting
 *   to the last valid state when an identifier match ends.
 */
public class IdentifierAnalyzer implements Analyzer<Object> {

    private Map<String,Object> keywords = new HashMap<>(Map.of("true", true, "false", false));
    private Predicate<Character> start = Character::isJavaIdentifierStart;
    private Predicate<Character> part = Character::isJavaIdentifierPart;

    public IdentifierAnalyzer() {
        keywords.put("null", null);
    }

    public void setKeywords(Map<String,Object> keywords) {
        this.keywords = keywords;
    }

    public Map<String,Object> keywords() {
        return keywords;
    }

    @Override
    public Token analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        final var start = input.position();
        Input.Index index = input.index();
        char ch = input.next();
        if (!this.start.test(ch)) {
            input.reset(index);
            return null;
        }
        final var sb = new StringBuilder();
        sb.append(ch);
        index = input.index();
        ch = input.next();

        while (part.test(ch)) {
            sb.append(ch);
            index = input.index();
            if (input.eof()) {
                break;
            }
            ch = input.next();
        }

        input.reset(index);
        final var end = input.position();
        if (sb.isEmpty()) {
            return null;
        }
        final var identifier = sb.toString();
        if (keywords.containsKey(identifier))
            return new Constant(start, end, keywords().get(identifier), identifier);
        else
            return new Identifier(start, end, sb.toString(), sb.toString());
    }
}

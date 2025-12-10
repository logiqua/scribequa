package com.javax0.logiqua.exp;

import com.javax0.lex.Analyzer;
import com.javax0.lex.Input;
import com.javax0.lex.Token;
import com.javax0.lex.tokens.Constant;
import com.javax0.lex.tokens.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ExpIdentifierAnalyzer implements Analyzer<Object> {

    private Map<String,Object> keywords = new HashMap<>(Map.of("true", true, "false", false));
    private final Predicate<Character> start = Character::isJavaIdentifierStart;
    private final Predicate<Character> part = Character::isJavaIdentifierPart;

    public ExpIdentifierAnalyzer() {
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

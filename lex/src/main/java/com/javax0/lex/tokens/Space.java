package com.javax0.lex.tokens;

import com.javax0.lex.Token;
import com.javax0.lex.Position;

public record Space(Position start, Position end, String value, String lexeme) implements Token<String> {

}

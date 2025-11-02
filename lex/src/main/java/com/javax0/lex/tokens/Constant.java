package com.javax0.lex.tokens;

import com.javax0.lex.Position;
import com.javax0.lex.Token;

public record Constant(Position start, Position end, String value, String lexeme) implements Token<String> {

}

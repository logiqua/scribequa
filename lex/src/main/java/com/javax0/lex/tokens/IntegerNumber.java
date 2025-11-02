package com.javax0.lex.tokens;

import com.javax0.lex.Token;
import com.javax0.lex.Position;

public record IntegerNumber(Position start, Position end, Long value, String lexeme) implements Token<Long> {
}

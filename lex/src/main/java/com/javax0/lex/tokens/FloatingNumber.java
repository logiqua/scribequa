package com.javax0.lex.tokens;

import com.javax0.lex.Position;
import com.javax0.lex.Token;

public record FloatingNumber(Position start, Position end, Double value, String lexeme) implements Token<Double> {
}

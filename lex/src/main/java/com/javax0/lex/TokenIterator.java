package com.javax0.lex;

public class TokenIterator {
    private final Token<?>[] elements;
    private int index = 0;

    public static TokenIterator over(Token<?>[] elements) {
        return new TokenIterator(elements);
    }

    public TokenIterator(Token<?>[] elements) {
        this.elements = elements;
    }

    public void step() {
        if (index < elements.length) {
            index++;
        }
    }

    public Token<?> current() {
        return elements[index];
    }

    public Token<?> next() {
        try {
            return current();
        } finally {
            step();
        }
    }

    public boolean eof(){
        return index >= elements.length;
    }


}

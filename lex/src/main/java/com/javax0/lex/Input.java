package com.javax0.lex;

/**
 * The Input interface defines the contract for sequentially reading characters
 * from an input source while maintaining positional information. This interface
 * facilitates controlled navigation and tracking of the current reading position,
 * with the ability to save and restore states.
 */
public interface Input {
    interface Index {}
    Position position();
    char peek();
    char peek(int i);
    char next();
    Index index();
    void reset(Index index);
    boolean eof();
    int length();
    boolean startsWith(String prefix);
}

package com.javax0.lex;

public interface Analyzer<T> {

    Token<T> analyse(Input input);

}

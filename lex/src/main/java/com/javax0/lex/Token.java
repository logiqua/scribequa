package com.javax0.lex;

/**
 * Represents a lexical token identified during lexical analysis.
 *
 * A token contains the
 *
 * <ul>
 *     <li>start position of the token, which is the position of the first character of the token
 *     <li>end position of the token which is the position of the first character AFTER the token
 *     <li>value is the value associated with the token. For example, a number, or a string after all the escaping took place
 *     <li>lexeme the characters from the input that make up the token
 * </ul>
 *
 * @param <T> the type of the value associated with this token
 */
public interface Token<T> {

    Position start();
    Position end();
    T value();
    String lexeme();
    default boolean is(final Class<? extends Token<?>> tokenClass) {
        return this.getClass().equals(tokenClass);
    }

    default boolean isOneOf(final Class<? extends Token<?>>... tokenClasses) {
        for (final var tokenClass : tokenClasses) {
            if (this.is(tokenClass)) {
                return true;
            }
        }
        return false;
    }
    default boolean is(final String lexeme) {
        return this.lexeme().equals(lexeme);
    }

    default boolean isOneOf(final String... lexemes) {
        for (final var lexeme : lexemes) {
            if (this.is(lexeme)) {
                return true;
            }
        }
        return false;
    }
}

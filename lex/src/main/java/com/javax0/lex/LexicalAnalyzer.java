package com.javax0.lex;

import java.util.*;

public class LexicalAnalyzer {

    private final List<Analyzer<?>> analyzers;

    private final Set<Class<? extends Token<?>>> skipped = new HashSet<>();

    public LexicalAnalyzer() {
        this.analyzers = new ArrayList<>();
        ServiceLoader.load(Analyzer.class).forEach(analyzers::add);
    }

    public void skip(Class<? extends Token<?>> type) {
        skipped.add(type);
    }

    /**
     * Retrieves an instance of the specified {@code Analyzer} type from the list of available analyzers.
     * If no matching analyzer is found, returns {@code null}.
     *
     * @param <T>  the type of the {@code Analyzer} to retrieve
     * @param type the {@code Class} object of the desired {@code Analyzer} type
     * @return an instance of the specified {@code Analyzer} type, or {@code null} if no such instance is found
     */
    public <T extends Analyzer<?>> T getAnalyzer(Class<T> type) {
        for (var a : analyzers) {
            if (a.getClass().equals(type)) {
                return type.cast(a);
            }
        }
        return null;
    }

    /**
     * Replaces an existing analyzer of the specified type with a new analyzer.
     * If no analyzer of the specified type is found in the list of analyzers,
     * an {@code IllegalArgumentException} is thrown.
     *
     * @param type        the type of the analyzer to be replaced
     * @param replacement the new analyzer that will replace the existing one
     * @throws IllegalArgumentException if the specified analyzer type is not registered
     */
    public void replaceAnalyzer(Class<?> type, Analyzer<?> replacement) {
        for (int i = 0; i < analyzers.size(); i++) {
            if (analyzers.get(i).getClass().equals(type)) {
                analyzers.set(i, replacement);
                return;
            }
        }
        throw new IllegalArgumentException("The analyzer " + type + " is not registered");
    }

    public Token<?>[] analyse(Input input) {
        final var result = new ArrayList<Token<?>>();
        while (!input.eof()) {
            Token<?> longest = null;
            Input.Index longestEnd = null;
            for (var analyzer : analyzers) {
                var index = input.index();
                var element = analyzer.analyse(input);
                if (element != null && !element.lexeme().isEmpty()) {
                    if (longest == null) {
                        longest = element;
                        longestEnd = input.index();
                    } else {
                        if (longest.lexeme().length() < element.lexeme().length()) {
                            longest = element;
                            longestEnd = input.index();
                        }
                    }
                }
                input.reset(index);
            }
            if (longest != null) {
                if (!skipped.contains(longest.getClass())) {
                    result.add(longest);
                }
                input.reset(longestEnd);
            } else {
                throw new IllegalArgumentException("No more lexical element found at position " + input.position());
            }
        }
        return result.toArray(Token[]::new);
    }


}

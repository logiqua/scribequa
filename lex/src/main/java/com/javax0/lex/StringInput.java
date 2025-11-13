package com.javax0.lex;

public class StringInput implements Input {
    private final String input;
    private int index;
    private Position position;

    record StringInputIndex(int index, Position position) implements Index {
    }

    public static StringInput of(String input) {
        return new StringInput(input);
    }

    private StringInput(String input) {
        this.input = input;
        position = new Position(null, 0, 0);
    }

    @Override
    public Position position() {
        return position;
    }

    @Override
    public char peek() {
        return input.charAt(index);
    }

    @Override
    public char next() {
        char ch = input.charAt(index++);
        if (ch == '\n') {
            position = position.nextLine();
        } else {
            position = position.nextCharacter();
        }
        return ch;
    }

    @Override
    public Index index() {
        return new StringInputIndex(index, position);
    }

    @Override
    public void reset(Index index) {
        if (index instanceof StringInputIndex(int i, Position pos)) {
            this.index = i;
            this.position = pos;
        } else {
            throw new IllegalArgumentException("The index must be of type StringInputIndex, it is" + index.getClass());
        }
    }

    @Override
    public boolean eof() {
        return index >= input.length();
    }

    @Override
    public boolean startsWith(String prefix) {
        if (index + prefix.length() <= input.length()) {
            return input.startsWith(prefix, index);
        }
        return false;
    }

    @Override
    public String toString() {
        return input.substring(index);
    }
}

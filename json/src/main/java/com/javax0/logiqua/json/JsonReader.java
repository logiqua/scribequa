package com.javax0.logiqua.json;

import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.*;

import java.util.*;

public class JsonReader {
    TokenIterator tokens;

    public static JsonReader of(final TokenIterator elements) {
        return new JsonReader(elements);
    }

    private JsonReader(final TokenIterator elements) {
        this.tokens = elements;
    }


    Object read() {
        if (tokens.current().is("{")) {
            return readObject();
        }
        if (tokens.current().is("[")) {
            return readArray();
        }
        final var token = tokens.next();
        if (token.is(Keyword.class)) {
            return switch (token.lexeme()) {
                case "true" -> token.value();
                case "false" -> token.value();
                case "null" -> token.value();
                default ->
                        throw new IllegalArgumentException("JSON cannot handle the keyword '" + token.lexeme() + "'");
            };
        }
        if (token.isOneOf(StringToken.class, IntegerNumber.class, FloatingNumber.class, Constant.class)) {
            return token.value();
        }
        throw new IllegalArgumentException("Cannot read the value from the token " + token);
    }


    List<Object> readArray() {
        final var array = new ArrayList<Object>();
        if (tokens.current().is("[")) {
            tokens.next();
            if (tokens.current().is("]")) {
                tokens.next();
                return List.of();
            }
            while (!tokens.eof()) {
                final var value = read();
                array.add(value);
                if (!tokens.eof()) {
                    if (tokens.current().is(",")) {
                        tokens.next();
                    } else if (tokens.current().is("]")) {
                        break;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("A JSON array  must start with '['");
        }

        if (!tokens.eof() && tokens.current().is("]")) {
            tokens.next(); // step over the closing ']'
        } else {
            throw new IllegalArgumentException("The JSON array must end with ']' before EOF");
        }

        return array;
    }

    Map<String, Object> readObject() {
        final var map = new LinkedHashMap<String, Object>();
        if (tokens.current().is("{")) {
            tokens.next();
            if (tokens.current().is("}")) {
                tokens.next();
                return Map.of();
            }
            while (!tokens.eof()) {
                final var key = tokens.next();
                if (key instanceof StringToken || key instanceof Identifier) {
                    final var keyStr = (String) key.value();
                    final var colon = tokens.next();
                    if (colon.is(":")) {
                        final var value = read();
                        map.put(keyStr, value);
                        if (!tokens.eof()) {
                            if (tokens.current().is(",")) {
                                tokens.next();
                            } else if (tokens.current().is("}")) {
                                break;
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("The value must be after the colon, like \"key\" : value\n" +
                                "but it is >>" + colon.lexeme() + "<< instead of a ':'");
                    }
                } else {
                    throw new IllegalArgumentException("The key must be a string or an identifier. It is >>" + key.lexeme() + "<< instead");
                }
            }

        } else {
            throw new IllegalArgumentException("A JSON object  must start with '{'");
        }
        if (tokens.eof()) {
            throw new IllegalArgumentException("The JSON object must end with '}' before EOF");
        } else {
            tokens.next(); // step over the closing '}'
        }
        return map;
    }
}

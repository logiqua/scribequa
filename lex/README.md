# Lex - General Purpose Pluggable Lexical Analyzer

A general-purpose, pluggable lexical analyzer (lexer) for Java that provides a flexible framework for tokenizing input text. The module uses a service loader pattern to automatically discover and register analyzers, making it easy to extend with custom token types.

## Overview

The `lex` module provides a framework for breaking down input text into tokens. It uses a pluggable architecture where different analyzers can recognize different types of tokens (identifiers, numbers, strings, symbols, etc.). The lexer automatically selects the longest matching token when multiple analyzers could match at a given position.

## Features

- **Pluggable Architecture**: Analyzers are automatically discovered using Java's ServiceLoader mechanism
- **Longest Match**: When multiple analyzers could match, the lexer automatically selects the longest match
- **Position Tracking**: Tracks file name, line, and column positions for each token
- **Token Skipping**: Ability to skip certain token types (e.g., whitespace) from the output
- **Customizable**: Replace or configure analyzers at runtime
- **Extensible**: Easy to add new token types by implementing the `Analyzer` interface

## Architecture

### Core Components

#### `LexicalAnalyzer`
The main class that orchestrates the tokenization process. It:
- Loads analyzers via ServiceLoader
- Processes input character by character
- Selects the longest matching token when multiple analyzers match
- Manages token skipping

#### `Analyzer<T>`
Interface that all token analyzers must implement. Each analyzer:
- Takes an `Input` object
- Returns a `Token<T>` if a match is found, or `null` otherwise
- Can be discovered automatically via ServiceLoader

#### `Token<T>`
Interface representing a lexical token with:
- **Start/End Position**: File location information
- **Value**: The semantic value of the token (e.g., parsed number)
- **Lexeme**: The raw characters that make up the token

#### `Input`
Interface for reading characters sequentially with:
- Position tracking
- Save/restore state (via `Index`)
- Peek ahead functionality
- EOF detection

### Built-in Analyzers

The module includes several built-in analyzers:

- **`IdentifierAnalyzer`**: Recognizes Java identifiers and keywords (true, false, null)
- **`IntegerNumberAnalyzer`**: Recognizes integer numbers (with optional +/- prefix)
- **`FloatNumberAnalyzer`**: Recognizes floating-point numbers
- **`StringAnalyzer`**: Recognizes string literals (with escape sequence handling)
- **`SymbolAnalyzer`**: Recognizes symbols (configurable symbol set)
- **`SpaceAnalyzer`**: Recognizes whitespace characters
- **`NewLineAnalyzer`**: Recognizes newline characters

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>lex</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

```java
import com.javax0.lex.*;

// Create input from a string
Input input = StringInput.of("Hello, World");

// Create lexical analyzer
LexicalAnalyzer lexer = new LexicalAnalyzer();

// Analyze input
Token<?>[] tokens = lexer.analyse(input);

// Process tokens
for (Token<?> token : tokens) {
    System.out.println(token);
}
```

### Skipping Tokens

You can skip certain token types (e.g., whitespace) from the output:

```java
LexicalAnalyzer lexer = new LexicalAnalyzer();
lexer.skip(Space.class);  // Skip spaces
lexer.skip(NewLine.class); // Skip newlines

Token<?>[] tokens = lexer.analyse(input);
```

### Configuring Analyzers

You can retrieve and configure analyzers:

```java
LexicalAnalyzer lexer = new LexicalAnalyzer();

// Configure symbol analyzer with custom symbols
SymbolAnalyzer symbolAnalyzer = lexer.getAnalyzer(SymbolAnalyzer.class);
symbolAnalyzer.setSymbols(new String[]{"<<", ">>", "=="});

// Configure identifier analyzer keywords
IdentifierAnalyzer idAnalyzer = lexer.getAnalyzer(IdentifierAnalyzer.class);
Map<String, Object> keywords = new HashMap<>();
keywords.put("if", "IF_KEYWORD");
keywords.put("else", "ELSE_KEYWORD");
idAnalyzer.setKeywords(keywords);
```

### Replacing Analyzers

You can replace built-in analyzers with custom implementations:

```java
class MyCustomAnalyzer implements Analyzer<Long> {
    @Override
    public Token<Long> analyse(Input input) {
        // Custom analysis logic
        return null; // or a token
    }
}

LexicalAnalyzer lexer = new LexicalAnalyzer();
lexer.replaceAnalyzer(IntegerNumberAnalyzer.class, new MyCustomAnalyzer());
```

## Extending the Lexer

### Creating a Custom Analyzer

1. Implement the `Analyzer<T>` interface:

```java
public class MyCustomAnalyzer implements Analyzer<MyType> {
    @Override
    public Token<MyType> analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        
        Position start = input.position();
        Input.Index index = input.index();
        
        // Try to match your pattern
        // If match found:
        //   Position end = input.position();
        //   return new MyToken(start, end, value, lexeme);
        
        // If no match:
        input.reset(index);
        return null;
    }
}
```

2. Create a corresponding `Token` implementation:

```java
public record MyToken(Position start, Position end, MyType value, String lexeme) 
    implements Token<MyType> {
}
```

3. Register the analyzer via ServiceLoader:
   - Create a file `META-INF/services/com.javax0.lex.Analyzer`
   - Add the fully qualified class name of your analyzer

### Example: Custom Analyzer

```java
package com.example;

import com.javax0.lex.*;

public class EmailAnalyzer implements Analyzer<String> {
    @Override
    public Token<String> analyse(Input input) {
        if (input.eof()) {
            return null;
        }
        
        Position start = input.position();
        Input.Index index = input.index();
        StringBuilder sb = new StringBuilder();
        
        // Simple email pattern: word@word.word
        // (This is simplified - real email validation is more complex)
        while (!input.eof() && isValidEmailChar(input.peek())) {
            sb.append(input.next());
        }
        
        String lexeme = sb.toString();
        if (lexeme.contains("@") && lexeme.length() > 3) {
            Position end = input.position();
            return new EmailToken(start, end, lexeme, lexeme);
        }
        
        input.reset(index);
        return null;
    }
    
    private boolean isValidEmailChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '@' || ch == '.' || ch == '_' || ch == '-';
    }
}
```

## Token Types

### Built-in Token Types

- **`Identifier`**: General identifiers (variable names, etc.)
- **`Constant`**: Keywords like `true`, `false`, `null`
- **`IntegerNumber`**: Integer values
- **`FloatingNumber`**: Floating-point values
- **`StringToken`**: String literals
- **`Symbol`**: Operators and punctuation
- **`Space`**: Whitespace characters
- **`NewLine`**: Newline characters
- **`Keyword`**: Reserved keywords (if configured)

## Building

The module is built using Maven:

```bash
mvn clean install
```

## Testing

Run the tests with:

```bash
mvn test
```

## Dependencies

- Java 25+
- JUnit Jupiter (for testing)

## License

See the main project LICENSE file for license information.

## See Also

- [API Documentation](target/reports/apidocs/index.html) - Full API reference
- Main project README for overall project information


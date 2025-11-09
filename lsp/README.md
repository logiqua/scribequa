# Logiqua LSP

The LSP module provides a LISP-style syntax parser for Logiqua. It allows you to write Logiqua expressions using a LISP-like syntax with parentheses and prefix notation, making it familiar to users of LISP, Scheme, or Clojure.

## Overview

The `lsp` module implements the `Logiqua` interface, parsing LISP-formatted expressions and converting them into Logiqua scripts. It uses the `lex` module for lexical analysis and provides a clean, functional syntax for writing expressions.

## Features

- **LISP-style Syntax**: Familiar prefix notation with parentheses
- **Automatic Operation Discovery**: Uses all standard Logiqua operations
- **Context Support**: Full support for variable access and data binding
- **Type Coercion**: Automatic type conversion for identifiers
- **Nested Expressions**: Support for complex nested expressions
- **Lexical Analysis**: Built on the `lex` module for robust tokenization

## Architecture

### Main Class

#### `LspLogiqua`
The main entry point for compiling LISP-style scripts:

```java
public class LspLogiqua implements Logiqua {
    public Engine engine();
    public LspLogiqua with(Engine engine);
    public LspLogiqua with(Map<String, Object> data);
    public Script compile(String source);
}
```

### Components

- **LspReader**: Parses tokens into LISP data structures (lists, values)
- **LspBuilder**: Converts LISP data structures into Logiqua scripts
- **LispSymbolAnalyzer**: Custom symbol analyzer for LISP syntax

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>lsp</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

#### Simple Expression

```java
LspLogiqua lsp = new LspLogiqua();
Script script = lsp.compile("(== 3 5)");
Object result = script.evaluate();
// result = false
```

#### With Context Data

```java
Map<String, Object> data = Map.of("x", 10, "y", 20);
LspLogiqua lsp = new LspLogiqua().with(data);
Script script = lsp.compile("(+ (var \"x\") (var \"y\"))");
Object result = script.evaluate();
// result = 30
```

#### Using Custom Engine

```java
Engine engine = Engine.withData(Map.of("x", 10));
LspLogiqua lsp = new LspLogiqua().with(engine);
Script script = lsp.compile("(var \"x\")");
Object result = script.evaluate();
// result = 10
```

## Syntax

### Basic Syntax

LISP-style expressions use prefix notation with parentheses:

```
(operation arg1 arg2 ...)
```

### Examples

#### Arithmetic Operations

```lisp
(+ 1 2 3)        ; Addition: 6
(- 10 3)         ; Subtraction: 7
(* 2 3 4)        ; Multiplication: 24
(/ 10 2)         ; Division: 5
(% 10 3)         ; Modulo: 1
```

#### Comparisons

```lisp
(== 3 5)         ; Equality: false
(!= 3 5)         ; Inequality: true
(< 3 5)          ; Less than: true
(<= 3 5)         ; Less than or equal: true
(> 5 3)          ; Greater than: true
(>= 5 3)         ; Greater than or equal: true
```

#### Logical Operations

```lisp
(and true true)   ; Logical AND: true
(or false true)   ; Logical OR: true
(! false)         ; Logical NOT: true
```

#### Conditional

```lisp
(if true "yes" "no")  ; Conditional: "yes"
```

#### Variable Access

```lisp
(var "x")                    ; Access variable x
(var "x" "default")          ; Access with default value
(var "user.name")            ; Nested access
(var "items[0]")             ; Array access
```

#### String Operations

```lisp
(cat "Hello" " " "World")     ; Concatenation: "Hello World"
(substr "Hello World" 0 5)   ; Substring: "Hello"
```

#### Array Operations

```lisp
; Filter
(filter (1 2 3 4 5)
        (== (% current 2) 0))  ; Even numbers: (2 4)

; Map
(map (1 2 3)
     (* current 2))            ; Double: (2 4 6)

; Reduce
(reduce (1 2 3)
        (+ accumulator current)
        0)                      ; Sum: 6

; Membership
(in "Ringo" ("John" "Paul" "George" "Ringo"))  ; true

; Quantifiers
(all (1 2 3) (> current 0))     ; All positive: true
(some (1 2 3) (> current 2))    ; Some > 2: true
(none (1 2 3) (> current 5))    ; None > 5: true
```

### Nested Expressions

Expressions can be nested to any depth:

```lisp
(if (and (< x 10) (> y 5))
    (+ x y)
    (- x y))
```

### Lists and Arrays

Lists are represented with parentheses:

```lisp
(1 2 3 4 5)                    ; List of numbers
("John" "Paul" "George")       ; List of strings
(true false true)              ; List of booleans
```

### Identifiers and Variables

Identifiers are automatically treated as variable references:

```lisp
; These are equivalent:
(var "x")
x                               ; Identifier automatically becomes (var "x")
```

### Symbols

Symbols (operators) are recognized automatically:

```lisp
(== 1 1)    ; == is a symbol
(+ 1 2)     ; + is a symbol
```

## Usage Examples

### Arithmetic

```java
LspLogiqua lsp = new LspLogiqua();
Script script = lsp.compile("(+ 1 2 3 4 5)");
Object result = script.evaluate();
// result = 15
```

### Comparisons

```java
LspLogiqua lsp = new LspLogiqua();
Script script = lsp.compile("(< 1 3 5)");
Object result = script.evaluate();
// result = true
```

### Logical Operations

```java
LspLogiqua lsp = new LspLogiqua();
Script script = lsp.compile("(and true true true)");
Object result = script.evaluate();
// result = true
```

### Conditional Logic

```java
LspLogiqua lsp = new LspLogiqua();
Script script = lsp.compile("(if (< 3 5) \"yes\" \"no\")");
Object result = script.evaluate();
// result = "yes"
```

### Variable Access

```java
Map<String, Object> data = Map.of("name", "Logiqua", "version", 1.0);
LspLogiqua lsp = new LspLogiqua().with(data);
Script script = lsp.compile("(cat (var \"name\") \" v\" (var \"version\"))");
Object result = script.evaluate();
// result = "Logiqua v1.0"
```

### Array Filtering

```java
LspLogiqua lsp = new LspLogiqua();
String script = """
    (filter (1 2 3 4 5 6 7 8 9)
            (== (% current 2) 0))
    """;
Script compiled = lsp.compile(script);
Object result = compiled.evaluate();
// result = [2, 4, 6, 8]
```

### Array Mapping

```java
LspLogiqua lsp = new LspLogiqua();
String script = """
    (map (23 24)
         (* current 2))
    """;
Script compiled = lsp.compile(script);
Object result = compiled.evaluate();
// result = [46, 48]
```

### Array Reduction

```java
LspLogiqua lsp = new LspLogiqua();
String script = """
    (reduce (1 2 3)
            (+ accumulator current)
            0)
    """;
Script compiled = lsp.compile(script);
Object result = compiled.evaluate();
// result = 6
```

### Membership Testing

```java
LspLogiqua lsp = new LspLogiqua();
Script script = lsp.compile("(in \"Ringo\" (\"John\" \"Paul\" \"George\" \"Ringo\"))");
Object result = script.evaluate();
// result = true
```

### String Operations

```java
LspLogiqua lsp = new LspLogiqua();
Script script = lsp.compile("(cat \"I love\" \" pie\")");
Object result = script.evaluate();
// result = "I love pie"
```

### Complex Nested Expressions

```java
LspLogiqua lsp = new LspLogiqua();
String script = """
    (if (and (< 1 5) (> 3 1))
        (+ 10 20)
        (- 10 20))
    """;
Script compiled = lsp.compile(script);
Object result = compiled.evaluate();
// result = 30
```

## Special Features

### Identifier to Variable Conversion

Identifiers are automatically converted to variable references:

```lisp
; These are equivalent:
(var "x")
x
```

This means you can use identifiers directly in expressions, and they will be treated as variable lookups.

### Local Context Variables

In array operations (`filter`, `map`, `reduce`, `all`, `some`, `none`), the current element is available as:

- `current`: The current element being processed
- `""` (empty string): Also refers to the current element
- `accumulator`: (For `reduce` only) The accumulator value

```lisp
(filter (1 2 3 4)
        (== (% current 2) 0))    ; Using "current"

(filter (1 2 3 4)
        (== (% "" 2) 0))         ; Using empty string (equivalent)
```

## Error Handling

The parser will throw `IllegalArgumentException` for:

- Invalid syntax
- Unclosed parentheses
- Extra text after the script
- Unknown operations
- Type mismatches

```java
try {
    Script script = lsp.compile("(invalid operation)");
    script.evaluate();
} catch (IllegalArgumentException e) {
    // Handle parsing or evaluation errors
}
```

## Integration with Logiqua

The LSP module is fully integrated with the Logiqua ecosystem:

- **Uses Engine**: Leverages the Logiqua engine for execution
- **Standard Operations**: All standard Logiqua operations are available
- **Context Support**: Full support for MapContext and custom contexts
- **Type Coercion**: Automatic type conversion via registered casters

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

- Java 21+
- Logiqua `api` module
- Logiqua `engine` module
- Logiqua `lex` module (for lexical analysis)
- JUnit Jupiter (for testing)

## License

See the main project LICENSE file for license information.

## See Also

- [API Documentation](target/reports/apidocs/index.html) - Full API reference
- [API Module README](../api/README.md) - Core API documentation
- [Engine Module README](../engine/README.md) - Execution engine documentation
- [Commands Module README](../commands/README.md) - Standard operations documentation
- [Lex Module README](../lex/README.md) - Lexical analysis documentation
- Main project README for overall project information


# Logiqua YAML

The YAML module provides a YAML-based syntax parser for Logiqua. It allows you to write Logiqua expressions using YAML format, making it human-readable and easy to edit. The syntax is compatible with JsonLogic format, making it easy to migrate from JsonLogic to Logiqua.

## Overview

The `yaml` module implements the `Logiqua` interface, parsing YAML-formatted expressions and converting them into Logiqua scripts. It uses SnakeYAML for parsing and provides a clean, declarative syntax for writing expressions.

## Features

- **YAML Syntax**: Human-readable, indentation-based syntax
- **JsonLogic Compatible**: Compatible with JsonLogic YAML format
- **Comments Support**: Full support for YAML comments (using `#`)
- **Automatic Operation Discovery**: Uses all standard Logiqua operations
- **Context Support**: Full support for variable access and data binding
- **Type Preservation**: Maintains numeric types (Long, Double) from YAML

## Architecture

### Main Class

#### `YamlLogiqua`
The main entry point for compiling YAML-style scripts:

```java
public class YamlLogiqua implements Logiqua {
    public Engine engine();
    public YamlLogiqua with(Engine engine);
    public YamlLogiqua with(Map<String, Object> data);
    public Script compile(String source);
}
```

### Components

- **YamlReader**: Parses YAML strings into Java objects using SnakeYAML
- **YamlBuilder**: Converts YAML data structures into Logiqua scripts

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>yaml</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

#### Simple Expression

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    "==":
      - 3
      - 5
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = false
```

#### With Context Data

```java
Map<String, Object> data = Map.of("x", 10, "y", 20);
YamlLogiqua yaml = new YamlLogiqua().with(data);
String script = """
    "+":
      - var: "x"
      - var: "y"
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = 30
```

#### Using Custom Engine

```java
Engine engine = Engine.withData(Map.of("x", 10));
YamlLogiqua yaml = new YamlLogiqua().with(engine);
String script = """
    var: "x"
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = 10
```

## Syntax

### Basic Syntax

YAML expressions use a map-based syntax where:

- **Key**: The operation name (e.g., `"+"`, `"=="`, `"var"`)
- **Value**: The arguments (can be a single value or a list)

```yaml
operation: argument
```

or

```yaml
operation:
  - arg1
  - arg2
  - arg3
```

### Comments

YAML expressions support comments using the `#` character:

```yaml
# This is a comment
"+":
  - 1  # First argument
  - 2  # Second argument
```

> **Note:** YAML expressions (parsed by SnakeYAML) support comments, which begin with the `#` character and continue to the end of that line. You can add comments in YAML scripts in the usual way.
>
> LSP expressions also support LISP-style comments using the semicolon `;` character.
>
> JSON expressions, however, do **not** support comments in any form—attempting to include comments in JSON will result in a parse error. If you want in-line documentation or explanations inside your rules, YAML or LSP formats are recommended.

### Examples

#### Arithmetic Operations

```yaml
# Addition
"+":
  - 1
  - 2
  - 3

# Subtraction
"-":
  - 10
  - 3

# Multiplication
"*":
  - 2
  - 3
  - 4

# Division
"/":
  - 10
  - 2

# Modulo
"%":
  - 10
  - 3
```

#### Comparisons

```yaml
# Equality
"==":
  - 3
  - 5

# Inequality
"!=":
  - 3
  - 5

# Less than
"<":
  - 3
  - 5

# Less than or equal
"<=":
  - 3
  - 5

# Greater than
">":
  - 5
  - 3

# Greater than or equal
">=":
  - 5
  - 3
```

#### Logical Operations

```yaml
# Logical AND
and:
  - true
  - true
  - false

# Logical OR
or:
  - false
  - true

# Logical NOT
"!": true
```

#### Conditional

```yaml
if:
  - true
  - "yes"
  - "no"
```

#### Variable Access

```yaml
# Simple access
var: "x"

# With default value
var:
  - "x"
  - "default"

# Nested access
var: "user.name"

# Array access
var: "items[0]"
```

#### String Operations

```yaml
# Concatenation
cat:
  - "Hello"
  - " "
  - "World"

# Substring
substr:
  - "Hello World"
  - 0
  - 5
```

#### Array Operations

```yaml
# Filter
filter:
  - - 1
    - 2
    - 3
    - 4
    - 5
  - "==":
    - "%":
      - var: ""
      - 2
    - 0

# Map
map:
  - - 23
    - 24
  - "*":
    - var: ""
    - 2

# Reduce
reduce:
  - - 1
    - 2
    - 3
  - "+":
    - var: "accumulator"
    - var: ""
  - 0

# Membership
in:
  - "Ringo"
  - - "John"
    - "Paul"
    - "George"
    - "Ringo"

# Quantifiers
all:
  - - 1
    - 2
    - 3
  - ">":
    - var: ""
    - 0

some:
  - - 1
    - 2
    - 3
  - ">":
    - var: ""
    - 2

none:
  - - 1
    - 2
    - 3
  - ">":
    - var: ""
    - 5
```

### Nested Expressions

Expressions can be nested to any depth:

```yaml
if:
  - and:
    - "<":
      - var: "x"
      - 10
    - ">":
      - var: "y"
      - 5
  - "+":
    - var: "x"
    - var: "y"
  - "-":
    - var: "x"
    - var: "y"
```

### Lists and Arrays

Lists are represented as YAML arrays:

```yaml
- 1
- 2
- 3
- 4
- 5
```

## Usage Examples

### Arithmetic

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    "+":
      - 1
      - 2
      - 3
      - 4
      - 5
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = 15
```

### Comparisons

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    "<":
      - 1
      - 3
      - 5
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = true
```

### Logical Operations

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    and:
      - true
      - true
      - true
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = true
```

### Conditional Logic

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    if:
      - "<":
        - 3
        - 5
      - "yes"
      - "no"
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = "yes"
```

### Variable Access

```java
Map<String, Object> data = Map.of("name", "Logiqua", "version", 1.0);
YamlLogiqua yaml = new YamlLogiqua().with(data);
String script = """
    cat:
      - var: "name"
      - " v"
      - var: "version"
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = "Logiqua v1.0"
```

### Array Filtering

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    filter:
      - - 1
        - 2
        - 3
        - 4
        - 5
        - 6
        - 7
        - 8
        - 9
      - "==":
        - "%":
          - var: ""
          - 2
        - 0
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = [2, 4, 6, 8]
```

### Array Mapping

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    map:
      - - 23
        - 24
      - "*":
        - var: ""
        - 2
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = [46, 48]
```

### Array Reduction

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    reduce:
      - - 1
        - 2
        - 3
      - "+":
        - var: "accumulator"
        - var: ""
      - 0
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = 6
```

### Complex Nested Expressions

```java
YamlLogiqua yaml = new YamlLogiqua();
String script = """
    if:
      - and:
        - "<":
          - 1
          - 5
        - ">":
          - 3
          - 1
      - "+":
        - 10
        - 20
      - "-":
        - 10
        - 20
    """;
Script compiled = yaml.compile(script);
Object result = compiled.evaluate();
// result = 30
```

## Special Features

### Local Context Variables

In array operations (`filter`, `map`, `reduce`, `all`, `some`, `none`), the current element is available as:

- `""` (empty string): The current element being processed
- `"current"`: Also refers to the current element
- `"accumulator"`: (For `reduce` only) The accumulator value

```yaml
filter:
  - - 1
    - 2
    - 3
    - 4
  - "==":
    - "%":
      - var: ""      # Current element
      - 2
    - 0
```

### Type Preservation

YAML numeric types are preserved:

- Integers are parsed as `Long`
- Floating-point numbers are parsed as `Double`

This ensures type consistency throughout the evaluation.

### JsonLogic Compatibility

The YAML format is compatible with JsonLogic YAML format, making it easy to migrate existing JsonLogic rules to Logiqua.

### Converting to JSON Format

Scripts compiled from YAML can be converted to JSON format using the `jsonify()` method, regardless of their original format. This enables canonical storage and JSON-based searchability:

```java
YamlLogiqua yaml = new YamlLogiqua().with(Map.of("x", 10, "y", 20));
Script script = yaml.compile("""
    "+":
      - var: "x"
      - var: "y"
    """);

// Convert to JSON format for storage
String jsonRepresentation = script.jsonify();
// jsonRepresentation = "{\"+\":[{\"var\":[\"x\"]},{\"var\":[\"y\"]}]}"
```

This allows applications to store scripts in a canonical JSON format while accepting input in YAML (or any other format), enabling format-independent script storage and searchability.

## Error Handling

The parser will throw `IllegalArgumentException` for:

- Invalid YAML syntax
- Unknown operations
- Type mismatches
- Missing required arguments

```java
try {
    Script script = yaml.compile(invalidYaml);
    script.evaluate();
} catch (IllegalArgumentException e) {
    // Handle parsing or evaluation errors
}
```

## Integration with Logiqua

The YAML module is fully integrated with the Logiqua ecosystem:

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
- SnakeYAML (for YAML parsing)
- JUnit Jupiter (for testing)

## License

See the main project LICENSE file for license information.

## See Also

- [API Documentation](target/reports/apidocs/index.html) - Full API reference
- [API Module README](../api/README.md) - Core API documentation
- [Engine Module README](../engine/README.md) - Execution engine documentation
- [Commands Module README](../commands/README.md) - Standard operations documentation
- Main project README for overall project information


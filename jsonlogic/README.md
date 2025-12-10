# Logiqua JsonLogic

The JsonLogic module provides API compatibility with the [JsonLogic](http://jsonlogic.com/) specification. This module allows you to evaluate JsonLogic expressions using the Logiqua execution engine, providing a bridge between JsonLogic's JSON-based rule format and Logiqua's powerful expression evaluation framework.

## Overview

The `jsonlogic` module implements JsonLogic-compatible operations while leveraging the Logiqua engine for execution. It parses JsonLogic JSON expressions and evaluates them with support for variables, logic operations, mathematical operations, comparisons, and more.

## Features

- **JsonLogic Compatibility**: Full compatibility with JsonLogic specification
- **Variable Access**: Access data from maps, arrays, and nested structures
- **Logic Operations**: Support for `and`, `or`, `if`, `not`, and other logical operations
- **Mathematical Operations**: Arithmetic operations (`+`, `-`, `*`, `/`, `%`, `min`, `max`)
- **Comparisons**: Equality, inequality, strict equality, and numeric comparisons
- **Array Operations**: Support for `in`, `some`, `all`, `none`, `filter`, `map`, `reduce`, etc.
- **String Operations**: Substring extraction and manipulation
- **Type Coercion**: Automatic type conversion (e.g., string to number)

## Architecture

### Main Class

#### `JsonLogic`
The main entry point for evaluating JsonLogic expressions:

```java
public class JsonLogic {
    public Object apply(String json, Object data) throws JsonLogicException;
    public static boolean truthy(Object value);
}
```

The `apply` method takes a JsonLogic expression as a JSON string and optional data, then evaluates and returns the result.

### Compatibility Commands

The module includes JsonLogic-compatible operations in the `compatibilitycommands` package:

- **Logic**: `JLAnd`, `JLOr`, `JLIf`, `JLNot`, `JLNotNot`
- **Variables**: `JLVar` - Access variables from data
- **Comparisons**: `JLEqual`, `JLStrictEqual`, `JLStrictInEqual`
- **Array Operations**: `JLIn`, `JLSome`, `JLNone`, `JLAll`
- **String Operations**: `JLSubstr`

All compatibility commands are prefixed with `JL` to avoid naming conflicts with Logiqua's native operations.

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>jsonlogic</artifactId>
    <version>2.0.1</version>
</dependency>
```

### Basic Usage

#### Simple Expression Evaluation

```java
JsonLogic jsonLogic = new JsonLogic();

// Evaluate a simple expression without data
Object result = jsonLogic.apply("""
    {"+": [4, 2]}
    """, null);
// result = 6
```

#### Using Variables

```java
JsonLogic jsonLogic = new JsonLogic();

// Define data
Map<String, Object> data = Map.of("pi", 3.14);

// Access variable from data
Object result = jsonLogic.apply("""
    {"var": "pi"}
    """, data);
// result = 3.14
```

#### Conditional Logic

```java
JsonLogic jsonLogic = new JsonLogic();

// If-else expression
Object result = jsonLogic.apply("""
    {"if": [true, "yes", "no"]}
    """, null);
// result = "yes"

// If-elseif-else
Object result = jsonLogic.apply("""
    {"if": [
        {"<": [50, 0]}, "freezing",
        {"<": [50, 100]}, "liquid",
        "gas"
    ]}
    """, null);
// result = "liquid"
```

#### Logic Operations

```java
JsonLogic jsonLogic = new JsonLogic();

// OR operation
Object result = jsonLogic.apply("""
    {"or": [0, false, "a"]}
    """, null);
// result = "a" (first truthy value)

// AND operation
Object result = jsonLogic.apply("""
    {"and": [true, "", 3]}
    """, null);
// result = "" (first falsy value)
```

#### Mathematical Operations

```java
JsonLogic jsonLogic = new JsonLogic();

// Addition
Object result = jsonLogic.apply("""
    {"+": [4, 2]}
    """, null);
// result = 6

// Multiple arguments
Object result = jsonLogic.apply("""
    {"+": [2, 2, 2, 2, 2]}
    """, null);
// result = 10

// Subtraction
Object result = jsonLogic.apply("""
    {"-": [4, 2]}
    """, null);
// result = 2

// Multiplication
Object result = jsonLogic.apply("""
    {"*": [4, 2]}
    """, null);
// result = 8

// Division
Object result = jsonLogic.apply("""
    {"/": [4, 2]}
    """, null);
// result = 2

// Modulo
Object result = jsonLogic.apply("""
    {"%": [101, 2]}
    """, null);
// result = 1

// Min/Max
Object result = jsonLogic.apply("""
    {"min": [1, 2, 3]}
    """, null);
// result = 1

Object result = jsonLogic.apply("""
    {"max": [1, 2, 3]}
    """, null);
// result = 3
```

#### Comparisons

```java
JsonLogic jsonLogic = new JsonLogic();

// Equality (with type coercion)
Object result = jsonLogic.apply("""
    {"==": [1, 1]}
    """, null);
// result = true

// Strict equality (no type coercion)
Object result = jsonLogic.apply("""
    {"===": [1, 1]}
    """, null);
// result = true

// Numeric comparisons
Object result = jsonLogic.apply("""
    {"<": [1, 2]}
    """, null);
// result = true

Object result = jsonLogic.apply("""
    {">": [2, 1]}
    """, null);
// result = true
```

#### Complex Variable Access

```java
JsonLogic jsonLogic = new JsonLogic();

// Nested map access
Map<String, Object> data = Map.of(
    "users", List.of(
        Map.of("name", "John", "followers", 1337),
        Map.of("name", "Jane", "followers", 2048)
    )
);

Object result = jsonLogic.apply("""
    {"var": "users.0.name"}
    """, data);
// result = "John"

Object result = jsonLogic.apply("""
    {"var": "users.1.followers"}
    """, data);
// result = 2048

// Default value when variable is missing
Object result = jsonLogic.apply("""
    {"var": ["missing.key", "default"]}
    """, null);
// result = "default"
```

#### Data as JSON String

The `apply` method can also accept data as a JSON string:

```java
JsonLogic jsonLogic = new JsonLogic();

String dataJson = """
    {"pi": 3.14, "name": "Logiqua"}
    """;

Object result = jsonLogic.apply("""
    {"var": "pi"}
    """, dataJson);
// result = 3.14
```

### Truthy Values

The module provides a static `truthy` method that determines if a value is truthy according to JsonLogic rules:

```java
boolean result = JsonLogic.truthy(null);        // false
boolean result = JsonLogic.truthy(false);       // false
boolean result = JsonLogic.truthy(true);        // true
boolean result = JsonLogic.truthy(0);           // false
boolean result = JsonLogic.truthy(1);           // true
boolean result = JsonLogic.truthy("");          // false
boolean result = JsonLogic.truthy("hello");     // true
boolean result = JsonLogic.truthy(List.of());   // false
boolean result = JsonLogic.truthy(List.of(1));  // true
```

## Supported Operations

### Logic Operations
- `and` - Logical AND
- `or` - Logical OR
- `if` - Conditional expression
- `not` - Logical NOT
- `!!` - Double negation (truthy conversion)

### Variable Access
- `var` - Access variables from data with support for:
  - Map keys: `{"var": "key"}`
  - Nested paths: `{"var": "users.0.name"}`
  - Default values: `{"var": ["key", "default"]}`
  - Empty string returns entire data object: `{"var": ""}`

### Mathematical Operations
- `+` - Addition
- `-` - Subtraction (unary or binary)
- `*` - Multiplication
- `/` - Division
- `%` - Modulo
- `min` - Minimum value
- `max` - Maximum value

### Comparison Operations
- `==` - Equality (with type coercion)
- `===` - Strict equality (no type coercion)
- `!=` - Inequality (with type coercion)
- `!==` - Strict inequality (no type coercion)
- `<` - Less than
- `<=` - Less than or equal
- `>` - Greater than
- `>=` - Greater than or equal

### Array Operations
- `in` - Check if value is in array
- `some` - Check if any element matches condition
- `all` - Check if all elements match condition
- `none` - Check if no elements match condition
- `filter` - Filter array elements
- `map` - Transform array elements
- `reduce` - Reduce array to single value

### String Operations
- `substr` - Extract substring

## Error Handling

The module throws `JsonLogicException` for errors:

```java
try {
    Object result = jsonLogic.apply(json, data);
} catch (JsonLogicException e) {
    System.err.println("Error: " + e.getMessage());
    System.err.println("Path: " + e.getJsonPath());
}
```

## Type Coercion

The module automatically handles type coercion, particularly for string-to-number conversion:

```java
// String "123" is automatically converted to number 123
Object result = jsonLogic.apply("""
    {"+": ["123", 1]}
    """, null);
// result = 124
```

## Integration with Logiqua

This module builds on top of the Logiqua `json` module and uses `JsonLogiqua` internally. It registers JsonLogic-compatible operations that override or extend the default Logiqua operations to ensure JsonLogic specification compliance.

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

The test suite includes comprehensive tests for all JsonLogic operations, ensuring compatibility with the JsonLogic specification.

## Dependencies

- Java 21+
- Logiqua `api` module
- Logiqua `engine` module
- Logiqua `commands` module
- Logiqua `json` module
- JUnit Jupiter (for testing)

## License

See the main project LICENSE file for license information.

## See Also

- [JsonLogic Specification](http://jsonlogic.com/) - Official JsonLogic documentation
- [API Documentation](target/reports/apidocs/index.html) - Full API reference
- Main project README for overall project information
- [API Module README](../api/README.md) - Core API documentation


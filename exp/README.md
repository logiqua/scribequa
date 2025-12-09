# Logiqua Expression

The Expression module provides an infix expression syntax parser for Logiqua. It allows you to write Logiqua expressions using a familiar mathematical notation with infix operators, making it intuitive for users familiar with programming languages like Java, JavaScript, or Python.

## Overview

The `exp` module implements the `Logiqua` interface, parsing infix-formatted expressions and converting them into Logiqua scripts. It uses the `lex` module for lexical analysis and provides a natural, readable syntax for writing expressions with proper operator precedence.

## Features

- **Infix Notation**: Familiar mathematical notation with operators between operands
- **Operator Precedence**: Proper handling of operator precedence and associativity
- **Automatic Operation Discovery**: Uses all standard Logiqua operations
- **Context Support**: Full support for variable access and data binding
- **Type Coercion**: Automatic type conversion for identifiers
- **Nested Expressions**: Support for complex nested expressions with parentheses
- **Function Calls**: Support for function-style operations (e.g., `var("x")`)
- **Comments**: Support for comments in expressions
- **Lexical Analysis**: Built on the `lex` module for robust tokenization

## Architecture

### Main Class

#### `ExpLogiqua`
The main entry point for compiling expression-style scripts:

```java
public class ExpLogiqua implements Logiqua {
    public Engine engine();
    public ExpLogiqua with(Engine engine);
    public ExpLogiqua with(Map<String, Object> data);
    public Script compile(String source);
}
```

### Components

- **ExpReader**: Parses tokens into expression trees respecting operator precedence
- **ExpBuilder**: Converts expression trees into Logiqua scripts
- **ExpSymbolAnalyzer**: Custom symbol analyzer for expression syntax
- **ExpCommentAnalyzer**: Handles comments in expressions
- **ExpIntegerNumberAnalyzer**: Custom integer number analyzer
- **ExpFloatNumberAnalyzer**: Custom floating-point number analyzer

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>exp</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

#### Simple Expression

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("3 == 5");
Object result = script.evaluate();
// result = false
```

#### With Context Data

```java
Map<String, Object> data = Map.of("x", 10, "y", 20);
ExpLogiqua exp = new ExpLogiqua().with(data);
Script script = exp.compile("x + y");
Object result = script.evaluate();
// result = 30
```

#### Using Custom Engine

```java
Engine engine = Engine.withData(Map.of("x", 10));
ExpLogiqua exp = new ExpLogiqua().with(engine);
Script script = exp.compile("var(\"x\")");
Object result = script.evaluate();
// result = 10
```

## Syntax

### Basic Syntax

Expressions use infix notation where operators appear between operands:

```
operand1 operator operand2
```

### Operator Precedence

The expression parser respects operator precedence, with higher precedence operators evaluated before lower precedence ones. Operators are grouped into precedence levels (from lowest to highest):

#### Precedence Level 0 (Lowest): Logical OR
- `or` - Logical OR operator

#### Precedence Level 1: Logical AND
- `and` - Logical AND operator

#### Precedence Level 2: Equality, Membership, and Array Operations
- !== - Strict inequality
- === - Strict equality
- == - Equality
- != - Inequality
- `in` - Membership test
- `all` - testing that all elements in the array satisfy the condition
- `none` - testing that none of the elements in the array satisfy the condition
- `some` - testing that at least one element in the array satisfies the condition
- `map` - mapping the array elements to a new array using the specified function
- `missing` - JSON logic "missing" function
- `missing_some` - JSON logic "missing_some" function

>**NOTE:** that all operators that are keywords can also be used as ordinary function calls.

#### Precedence Level 3: Comparison
- <- Less than
- <= - Less than or equal
- \> - Greater than
- \>= - Greater than or equal

#### Precedence Level 4: Addition and Subtraction
- `+` - Addition (binary) or unary plus
- `-` - Subtraction (binary) or unary minus

#### Precedence Level 5 (Highest): Multiplication, Division, Modulo
- `*` - Multiplication
- `/` - Division
- `%` - Modulo

#### Unary Operators
- `!` - Logical NOT (lowest precedence, evaluated last)
- `+` - Unary plus
- `-` - Unary minus

### Precedence Examples

The parser correctly handles operator precedence:

```java
// Multiplication has higher precedence than addition
// 63 * 14 + 3 = (63 * 14) + 3 = 882 + 3 = 885
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("63*14+3");
Object result = script.evaluate();
// result = 885
```

```java
// Comparison has lower precedence than arithmetic
// 1 + 2 < 5 = (1 + 2) < 5 = 3 < 5 = true
Script script = exp.compile("1 + 2 < 5");
Object result = script.evaluate();
// result = true
```

```java
// Logical AND has lower precedence than comparison
// 1 < 5 and 3 < 7 = (1 < 5) and (3 < 7) = true and true = true
Script script = exp.compile("1 < 5 and 3 < 7");
Object result = script.evaluate();
// result = true
```

```java
// Logical OR has lower precedence than AND
// true and true or false = (true and true) or false = true or false = true
Script script = exp.compile("true and true or false");
Object result = script.evaluate();
// result = true
```

### Examples

#### Arithmetic Operations

```java
// Addition
exp.compile("1 + 2 + 3")        // 6

// Subtraction
exp.compile("10 - 3")            // 7

// Multiplication
exp.compile("2 * 3 * 4")        // 24

// Division
exp.compile("10 / 2")            // 5

// Modulo
exp.compile("10 % 3")            // 1

// Unary minus
exp.compile("-5")                // -5

// Unary plus
exp.compile("+5")                // 5
```

#### Comparisons

```java
// Equality
exp.compile("3 == 5")            // false

// Inequality
exp.compile("3 != 5")           // true

// Less than
exp.compile("3 < 5")             // true

// Less than or equal
exp.compile("3 <= 5")            // true

// Greater than
exp.compile("5 > 3")             // true

// Greater than or equal
exp.compile("5 >= 3")            // true

// Multiple comparisons (chained)
exp.compile("1 < 3 and 3 < 5")   // true
```

#### Logical Operations

```java
// Logical AND
exp.compile("true and true")     // true
exp.compile("true and false")    // false

// Logical OR
exp.compile("false or true")     // true
exp.compile("false or false")    // false

// Logical NOT
exp.compile("! true")            // false
exp.compile("! false")           // true

// Complex logical expressions
exp.compile("! true or (true and true)")  // true
exp.compile("! (true and true)")           // false
```

#### Conditional

```java
// Using function call syntax
exp.compile("if(true, \"yes\", \"no\")")  // "yes"
```

It is to note that the `if` function evaluates only one of the second and third arguments based on the result of the first argument.
Although the whole design is to query the values and do not provide mutability, there can be some side effects.
For example, the following code will result in zero when `a` is zero, because it does not evaluate the third argument, and hence it does not throw an error:

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("if( a == 0, 0, 1.0 / a)");
Object result = script.evaluate();
// result = 0 when a is 0 (no division by zero error)
```

#### Variable Access

Variables can be accessed directly using their name or through the use of the `var` function:

```java
// Direct identifier access (automatically converted to var)
Map<String, Object> data = Map.of("x", 10, "y", 20);
ExpLogiqua exp = new ExpLogiqua().with(data);
exp.compile("x * y")             // 200

// Function call syntax
exp.compile("var(\"x\")")         // 10
exp.compile("var(\"x\", \"default\")")  // 10 (or default if x not found)

// Nested access
exp.compile("var(\"user.name\")")

// Array access
exp.compile("var(\"items[0]\")")
```

Note that compound variables, like array access or sub-maps can only be accessed using the `var` function.
Simple variables, that are identifiers, can be accessed directly.

#### Function Calls

Functions are called using the standard function call syntax with parentheses:

```java
// String concatenation
exp.compile("cat(\"Hello\", \" \", \"World\")")  // "Hello World"

// Substring
exp.compile("substr(\"Hello World\", 0, 5)")     // "Hello"

// Variable access
exp.compile("var(\"x\")")                         // Access variable x
```

#### Lists and Arrays

Lists are represented with square brackets:

```java
// List literal
exp.compile("[1, 2, 3, 4, 5]")   // [1, 2, 3, 4, 5]

// Empty list
exp.compile("[]")                 // []
```

Array constants can be included in the code listing the values between `[` and `]` separated by commas.

#### Array Operations

```java
// Filter
exp.compile("filter([1, 2, 3, 4, 5], var(\"\") % 2 == 0)")  // [2, 4]

// Map
exp.compile("map([23, 24], var(\"\") * 2)")                   // [46, 48]

// Reduce
exp.compile("reduce([1, 2, 3], var(\"accumulator\") + var(\"\"), 0)")  // 6

// Membership
exp.compile("\"Ringo\" in [\"John\", \"Paul\", \"George\", \"Ringo\"]")  // true

// Quantifiers
exp.compile("all([1, 2, 3], var(\"\") > 0)")     // true
exp.compile("some([1, 2, 3], var(\"\") > 2)")    // true
exp.compile("none([1, 2, 3], var(\"\") > 5)")   // true
```

### Nested Expressions

Expressions can be nested using parentheses to override precedence:

```java
// Parentheses override precedence
exp.compile("(1 + 2) * 3")        // 9 (not 7)

// Complex nested expressions
exp.compile("((63 * 14)) + ((3))")  // 885

// Logical expressions with parentheses
exp.compile("(! true) or (true and true)")  // true
exp.compile("! (true and true)")            // false
```

### Comments

The expression parser supports comments:

```java
// Single-line comments are supported
exp.compile("3 + 5 # This is a comment")
```

Anything following a `#` is ignored until the end of the line.

## Usage Examples

### Arithmetic

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("1 + 2 + 3 + 4 + 5");
Object result = script.evaluate();
// result = 15
```

### Comparisons

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("1 < 3 and 3 < 5");
Object result = script.evaluate();
// result = true
```

### Logical Operations

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("true and true and true");
Object result = script.evaluate();
// result = true
```

### Conditional Logic

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("if(3 < 5, \"yes\", \"no\")");
Object result = script.evaluate();
// result = "yes"
```

### Variable Access

```java
Map<String, Object> data = Map.of("name", "Logiqua", "version", 1.0);
ExpLogiqua exp = new ExpLogiqua().with(data);
Script script = exp.compile("cat(var(\"name\"), \" v\", var(\"version\"))");
Object result = script.evaluate();
// result = "Logiqua v1.0"
```

### Direct Identifier Access

```java
Map<String, Object> data = Map.of("foo", 42, "bar", 44);
ExpLogiqua exp = new ExpLogiqua().with(data);
Script script = exp.compile("foo * bar");
Object result = script.evaluate();
// result = 1848
```

### Array Filtering

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("filter([1, 2, 3, 4, 5, 6, 7, 8, 9], var(\"\") % 2 == 0)");
Object result = script.evaluate();
// result = [2, 4, 6, 8]
```

### Array Mapping

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("map([23, 24], var(\"\") * 2)");
Object result = script.evaluate();
// result = [46, 48]
```

### Array Reduction

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("reduce([1, 2, 3], var(\"accumulator\") + var(\"\"), 0)");
Object result = script.evaluate();
// result = 6
```

### Complex Nested Expressions

```java
ExpLogiqua exp = new ExpLogiqua();
Script script = exp.compile("if((1 < 5) and (3 > 1), 10 + 20, 10 - 20)");
Object result = script.evaluate();
// result = 30
```

### Operator Precedence

```java
ExpLogiqua exp = new ExpLogiqua();
// Multiplication has higher precedence than addition
Script script = exp.compile("63 * 14 + 3");
Object result = script.evaluate();
// result = 885 (equivalent to (63 * 14) + 3)
```

## Special Features

### Identifier to Variable Conversion

Identifiers are automatically converted to variable references:

```java
// These are equivalent when x is in the context:
exp.compile("x")
exp.compile("var(\"x\")")
```

This means you can use identifiers directly in expressions, and they will be treated as variable lookups.

### Converting to JSON Format

Scripts compiled from infix expressions can be converted to JSON format using the `jsonify()` method, regardless of their original format. This enables canonical storage and JSON-based searchability:

```java
ExpLogiqua exp = new ExpLogiqua().with(Map.of("x", 10, "y", 20));
Script script = exp.compile("x + y");

// Convert to JSON format for storage
String jsonRepresentation = script.jsonify();
// jsonRepresentation = "{\"+\":[{\"var\":[\"x\"]},{\"var\":[\"y\"]}]}"
```

This allows applications to store scripts in a canonical JSON format while accepting input in infix expression format (or any other format), enabling format-independent script storage and searchability.

### Local Context Variables

In array operations (`filter`, `map`, `reduce`, `all`, `some`, `none`), the current element is available as:

- `""` (empty string): The current element being processed
- `"current"`: Also refers to the current element
- `"accumulator"`: (For `reduce` only) The accumulator value

```java
// Using empty string for current element
exp.compile("filter([1, 2, 3, 4], var(\"\") % 2 == 0)")

// Using accumulator in reduce
exp.compile("reduce([1, 2, 3], var(\"accumulator\") + var(\"\"), 0)")
```

### Operator Precedence and Associativity

Operators are evaluated according to their precedence level, with higher precedence operators evaluated first. When operators have the same precedence, they are typically left-associative (evaluated left to right).

Use parentheses to explicitly control evaluation order:

```java
// Without parentheses: multiplication first
exp.compile("2 + 3 * 4")         // 14 (not 20)

// With parentheses: addition first
exp.compile("(2 + 3) * 4")       // 20
```

## Error Handling

The parser will throw `IllegalArgumentException` for:

- Invalid syntax
- Unclosed parentheses or brackets
- Extra text after the script
- Unknown operations
- Type mismatches
- Invalid operator usage

```java
try {
    Script script = exp.compile("invalid expression");
    script.evaluate();
} catch (IllegalArgumentException e) {
    // Handle parsing or evaluation errors
}
```

## Integration with Logiqua

The Expression module is fully integrated with the Logiqua ecosystem:

- **Uses Engine**: Leverages the Logiqua engine for execution
- **Standard Operations**: All standard Logiqua operations are available
- **Context Support**: Full support for MapContext and custom contexts
- **Type Coercion**: Automatic type conversion via registered casters
- **Lexical Analysis**: Uses the `lex` module for tokenization

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


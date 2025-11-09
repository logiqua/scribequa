# Logiqua Commands

The Commands module provides a comprehensive set of built-in operations (functions and macros) for the Logiqua expression engine. These operations are automatically discovered and loaded via Java's ServiceLoader mechanism, making them available to all Logiqua implementations.

## Overview

The `commands` module contains the standard library of operations for Logiqua. It includes:

- **Logic Operations**: Conditional logic, boolean operations
- **Mathematical Operations**: Arithmetic, comparisons, min/max
- **Variable Access**: Reading values from context
- **Array Operations**: Filtering, mapping, reducing, membership testing
- **String Operations**: Concatenation, substring extraction
- **Utility Operations**: Logging, missing value detection

All operations are automatically registered when the module is on the classpath, thanks to the ServiceLoader mechanism.

## Features

- **Automatic Discovery**: Operations are automatically loaded via ServiceLoader
- **Function Operations**: Pre-evaluated arguments (functions)
- **Macro Operations**: Unevaluated arguments for control flow (macros)
- **Type Coercion**: Automatic type conversion where appropriate
- **Comprehensive Coverage**: Operations for logic, math, arrays, strings, and more

## Architecture

### Operation Types

The module provides two types of operations:

1. **Functions** (`Operation.Function`): Arguments are evaluated before the function is called
2. **Macros** (`Operation.Macro`): Receive unevaluated arguments and can control evaluation

### ServiceLoader Integration

Operations are registered via ServiceLoader configuration files:

- `META-INF/services/com.javax0.logiqua.Operation$Function` - Lists all function operations
- `META-INF/services/com.javax0.logiqua.Operation$Macro` - Lists all macro operations

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>commands</artifactId>
    <version>1.0.0</version>
</dependency>
```

> **Note:** If you are using the `engine`, `json`, `xml`, `yaml`, or other Logiqua modules, the `commands` module is typically included as a transitive dependency. You only need to add it explicitly if you want to use the operations directly or ensure they are available.

### Automatic Loading

Operations are automatically loaded when the module is on the classpath. No additional configuration is required. The engine will discover and register all operations during initialization.

## Available Operations

### Logic Operations

#### `if` (Macro)
Conditional expression: `if(condition, thenValue, elseValue)`

```java
// If condition is true, return thenValue, otherwise return elseValue
// If elseValue is omitted, returns null when condition is false
```

**Arity**: 2-3 arguments

#### `and` (Macro)
Logical AND: Returns `true` if all arguments are truthy, `false` otherwise

```java
// Short-circuits: returns false on first falsy value
```

**Arity**: 2+ arguments

#### `or` (Macro)
Logical OR: Returns first truthy value, or `false` if all are falsy

```java
// Short-circuits: returns first truthy value
```

**Arity**: 2+ arguments

#### `not` (Function)
Logical NOT: Returns the boolean negation of the argument

**Arity**: 1 argument

### Comparison Operations

#### `==` (Function)
Equality: Returns `true` if all arguments are equal (with type coercion)

**Arity**: 2+ arguments

#### `!=` (Function)
Inequality: Returns `true` if arguments are not equal (with type coercion)

**Arity**: 2 arguments

#### `<` (Function)
Less than: Returns `true` if first argument is less than second

**Arity**: 2 arguments

#### `<=` (Function)
Less than or equal: Returns `true` if first argument is less than or equal to second

**Arity**: 2 arguments

#### `>` (Function)
Greater than: Returns `true` if first argument is greater than second

**Arity**: 2 arguments

#### `>=` (Function)
Greater than or equal: Returns `true` if first argument is greater than or equal to second

**Arity**: 2 arguments

#### `between` (Function)
Between: Returns `true` if value is between min and max (inclusive)

**Arity**: 3 arguments

### Mathematical Operations

#### `+` (Function)
Addition: Adds all arguments together

```java
// Supports numeric types and string concatenation
// Single argument returns the value (or negates if string represents a number)
```

**Arity**: 1+ arguments

#### `-` (Function)
Subtraction: Subtracts arguments from the first

```java
// Single argument negates the value
```

**Arity**: 1+ arguments

#### `*` (Function)
Multiplication: Multiplies all arguments together

**Arity**: 2+ arguments

#### `/` (Function)
Division: Divides first argument by subsequent arguments

**Arity**: 2+ arguments

#### `%` (Function)
Remainder: Returns the remainder of division

**Arity**: 2 arguments

#### `min` (Function)
Minimum: Returns the minimum value from arguments

**Arity**: 2+ arguments

#### `max` (Function)
Maximum: Returns the maximum value from arguments

**Arity**: 2+ arguments

### Variable Access

#### `var` (Function)
Variable access: Retrieves a value from the context by key

```java
// var("key") - Returns value for key, throws if missing
// var("key", defaultValue) - Returns value for key, or defaultValue if missing
// Supports nested access: var("key.subkey[0]")
```

**Arity**: 1-2 arguments

### Array Operations

#### `in` (Function)
Membership test: Returns `true` if first argument is in the second (array or string)

```java
// in(value, array) - Checks if value is in array
// in(substring, string) - Checks if substring is in string
```

**Arity**: 2 arguments

#### `filter` (Macro)
Filter array: Returns a new array containing elements that match the condition

```java
// filter(array, conditionScript)
// In the condition script, use "current" or "" to access the current element
```

**Arity**: 2 arguments

#### `map` (Macro)
Transform array: Returns a new array with transformed elements

```java
// map(array, transformScript)
// In the transform script, use "current" or "" to access the current element
```

**Arity**: 2 arguments

#### `reduce` (Macro)
Reduce array: Reduces array to a single value using an accumulator

```java
// reduce(array, reduceScript, initialValue)
// In the reduce script, use "current" or "" for current element, "accumulator" for accumulator
```

**Arity**: 3 arguments

#### `all` (Macro)
All match: Returns `true` if all elements match the condition

```java
// all(array, conditionScript)
// Short-circuits: returns false on first non-matching element
```

**Arity**: 2 arguments

#### `some` (Macro)
Some match: Returns `true` if any element matches the condition

```java
// some(array, conditionScript)
// Short-circuits: returns true on first matching element
```

**Arity**: 2 arguments

#### `none` (Macro)
None match: Returns `true` if no elements match the condition

```java
// none(array, conditionScript)
// Short-circuits: returns false on first matching element
```

**Arity**: 2 arguments

### String Operations

#### `cat` (Function)
Concatenation: Concatenates all string arguments

**Arity**: 1+ arguments

#### `substr` (Function)
Substring: Extracts a substring from a string

```java
// substr(string, start) - From start to end of string
// substr(string, start, length) - From start with specified length
// Negative indices count from the end
```

**Arity**: 2-3 arguments

### Utility Operations

#### `log` (Function)
Logging: Prints all arguments to standard output and returns the first argument

**Arity**: 1+ arguments

#### `missing` (Function)
Missing keys: Returns an array of keys that are missing from the context

```java
// missing("key1", "key2", ...) - Returns array of missing keys
// missing(arrayOfKeys) - Returns array of missing keys from array
```

**Arity**: 1+ arguments

#### `missingSome` (Function)
Missing some: Returns `true` if at least the specified number of keys are missing

```java
// missingSome(minMissing, "key1", "key2", ...)
// Returns true if at least minMissing keys are missing
```

**Arity**: 2+ arguments

#### `merge` (Function)
Merge arrays: Merges multiple arrays into a single array

**Arity**: 1+ arguments

#### `selectOne` (Function)
Select one: Returns the first non-null value from arguments

**Arity**: 2+ arguments

## Usage Examples

### Logic Operations

```java
// Conditional
if(condition, "yes", "no")

// Boolean logic
and(true, true, false)  // false
or(false, false, true)  // true
not(false)  // true
```

### Mathematical Operations

```java
// Arithmetic
+(1, 2, 3)  // 6
-(10, 3)    // 7
*(2, 3, 4)  // 24
/(10, 2)    // 5
%(10, 3)    // 1

// Min/Max
min(1, 2, 3)  // 1
max(1, 2, 3)  // 3
```

### Variable Access

```java
// Simple access
var("name")

// With default
var("name", "Unknown")

// Nested access
var("user.address.city")
var("items[0].name")
```

### Array Operations

```java
// Filter
filter([1, 2, 3, 4], >(current, 2))  // [3, 4]

// Map
map([1, 2, 3], *(current, 2))  // [2, 4, 6]

// Reduce
reduce([1, 2, 3], +(accumulator, current), 0)  // 6

// Membership
in(2, [1, 2, 3])  // true
in("lo", "hello")  // true

// Quantifiers
all([1, 2, 3], >(current, 0))  // true
some([1, 2, 3], >(current, 2))  // true
none([1, 2, 3], >(current, 5))  // true
```

### String Operations

```java
// Concatenation
cat("Hello", " ", "World")  // "Hello World"

// Substring
substr("Hello World", 0, 5)  // "Hello"
substr("Hello World", -5)    // "World"
```

### Utility Operations

```java
// Logging
log("Debug:", value)  // Prints to stdout, returns value

// Missing keys
missing("key1", "key2")  // Returns array of missing keys
missingSome(2, "key1", "key2", "key3")  // true if at least 2 are missing

// Merge
merge([1, 2], [3, 4])  // [1, 2, 3, 4]

// Select first non-null
selectOne(null, null, "value")  // "value"
```

## Local Executor Context

Array operations (`filter`, `map`, `reduce`, `all`, `some`, `none`) create a local execution context with special variables:

- `current` or `""`: The current element being processed
- `accumulator`: (For `reduce` only) The accumulator value

These variables are available in the script passed to the operation.

## Type Coercion

Many operations support automatic type coercion:

- **Numeric types**: Automatic conversion between numeric types
- **String to number**: Strings representing numbers are converted
- **Boolean**: Type casters can convert values to boolean
- **Context casters**: Uses registered casters from the context

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
- JUnit Jupiter (for testing)

## License

See the main project LICENSE file for license information.

## See Also

- [API Documentation](target/reports/apidocs/index.html) - Full API reference
- [API Module README](../api/README.md) - Core API documentation
- [Engine Module README](../engine/README.md) - Execution engine documentation
- Main project README for overall project information


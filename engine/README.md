# Logiqua Engine

The Engine module provides the core execution engine for Logiqua. It implements the `Executor` and `Builder` interfaces from the API module, managing operation registration, script building, and execution with context support.

## Overview

The `engine` module is the heart of the Logiqua execution framework. It provides:

- **Operation Management**: Automatic loading and registration of operations via ServiceLoader
- **Script Building**: Fluent API for programmatically building scripts
- **Context Management**: Flexible context system for variable access and data handling
- **Type Casting**: Automatic type conversion between compatible types
- **Proxy Support**: Extensible proxy system for accessing custom object types

## Features

- **ServiceLoader Integration**: Automatically discovers and loads operations from the classpath
- **Operation Registry**: Centralized registry for managing operations (functions and macros)
- **MapContext**: Map-based context implementation with support for nested access
- **Type Casting**: Built-in type casters for numeric types and extensible caster registry
- **Proxy System**: Register proxies for custom object types to enable map-like or list-like access
- **Fluent Builder API**: Programmatic script construction with a clean, fluent interface

## Architecture

### Core Classes

#### `Engine`
The main execution engine that implements both `Executor` and `Builder`:

```java
public class Engine implements Executor, Builder {
    public static Engine withData(Map<String, Object> map);
    public static Engine withData(Context context);
    public Context getContext();
    public Optional<Operation> getOperation(String symbol);
    public void updateOperation(Operation operation);
    public void registerOperation(Operation operation);
    public NodeBuilder getOp(String symbol);
    public ConstantValueNode<?> constant(Object value);
}
```

The Engine automatically loads operations from the ServiceLoader mechanism on initialization.

#### `MapContext`
A context implementation that uses a `Map<String, Object>` for data storage:

```java
public class MapContext implements Context {
    public MapContext(Map<String, Object> map);
    public Context.Value get(String key);
    public Context.Accessor accessor(Object target);
    public <From, To> Optional<Context.Caster<From, To>> caster(Class<From> from, Class<To> to);
    public <From, To> void registerCaster(Class<From> from, Class<To> to, Context.Caster<From, To> caster);
    public void registerProxy(Class<?> forInterface, ProxyFactory proxy);
}
```

MapContext supports:
- **Nested key access**: `"key.subkey"` for accessing nested maps
- **Array indexing**: `"key[index]"` for accessing list/array elements
- **Mixed access**: `"key.subkey[0].field"` for complex nested structures
- **Type casting**: Automatic conversion between compatible types
- **Proxy registration**: Custom access patterns for non-standard objects

#### `Registry`
Manages the collection of registered operations:

```java
public class Registry {
    public void register(Operation operation);
    public void update(Operation operation);
    public void registerOrUpdate(Operation operation);
    public Operation get(String symbol);
}
```

#### `Builder`
Interface for programmatically building scripts:

```java
public interface Builder {
    NodeBuilder getOp(String symbol);
    ConstantValueNode<?> constant(Object value);
}
```

The Builder provides a fluent API for constructing scripts programmatically.

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>engine</artifactId>
    <version>2.0.1</version>
</dependency>
```

### Basic Usage

#### Creating an Engine

```java
// Create engine with empty context
Engine engine = Engine.withData(Map.of());

// Create engine with data
Map<String, Object> data = Map.of("name", "Logiqua", "version", 1.0);
Engine engine = Engine.withData(data);

// Create engine with custom context
Context customContext = new MyCustomContext();
Engine engine = Engine.withData(customContext);
```

#### Building and Evaluating Scripts

```java
Engine engine = Engine.withData(Map.of());

// Build a script using the fluent API
Script script = engine.getOp("log")
    .args(engine.constant("Hello, World!"));

// Evaluate the script
Object result = script.evaluate();
```

#### Accessing Variables

```java
// Simple variable access
Map<String, Object> data = Map.of("a", "Hello, World!");
Engine engine = Engine.withData(data);
Script script = engine.getOp("var").args("a");
Object result = script.evaluate();
// result = "Hello, World!"

// Nested map access
Map<String, Object> data = Map.of(
    "b", Map.of("german", "Wilcommen, Welt")
);
Engine engine = Engine.withData(data);
Script script = engine.getOp("var").args("b.german");
Object result = script.evaluate();
// result = "Wilcommen, Welt"

// Array/list access
Map<String, Object> data = Map.of(
    "b", List.of("german", "Wilcommen, Welt")
);
Engine engine = Engine.withData(data);
Script script = engine.getOp("var").args("b[1]");
Object result = script.evaluate();
// result = "Wilcommen, Welt"

// Complex nested access
Map<String, Object> data = Map.of(
    "b", List.of("german", List.of("Wilcommen, Welt"))
);
Engine engine = Engine.withData(data);
Script script = engine.getOp("var").args("b[1][0]");
Object result = script.evaluate();
// result = "Wilcommen, Welt"
```

#### Conditional Logic

```java
Engine engine = Engine.withData(Map.of());

// Simple if-else
Script script = engine.getOp("if")
    .args(true, "Very well", "Too bad");
Object result = script.evaluate();
// result = "Very well"

// Using variables in conditions
Map<String, Object> data = Map.of("a", true);
Engine engine = Engine.withData(data);
Script script = engine.getOp("if")
    .args(
        engine.getOp("var").args("a"),
        "Hello, World!",
        "Goodbye"
    );
Object result = script.evaluate();
// result = "Hello, World!"
```

#### Building Complex Scripts

```java
Engine engine = Engine.withData(Map.of("x", 10, "y", 20));

// Build a script with multiple operations
Script script = engine.getOp("+")
    .args(
        engine.getOp("var").args("x"),
        engine.getOp("var").args("y")
    );

Object result = script.evaluate();
// result = 30
```

#### Converting Scripts to JSON Format

The `jsonify()` method allows you to convert any script to JSON format for canonical storage:

```java
Engine engine = Engine.withData(Map.of("x", 10, "y", 20));

// Build a script programmatically
Script script = engine.getOp("+")
    .args(
        engine.getOp("var").args("x"),
        engine.getOp("var").args("y")
    );

// Convert to JSON format
String jsonRepresentation = script.jsonify();
// jsonRepresentation = "{\"+\":[{\"var\":[\"x\"]},{\"var\":[\"y\"]}]}"

// This JSON can be stored in a database or searched using JSON query tools
```

This is particularly useful when you want to:
- Store scripts in a canonical format regardless of how they were created
- Enable JSON-based searchability of scripts
- Maintain format independence in your application's script storage

### Context Features

#### Type Casting

MapContext includes built-in casters for numeric types and allows custom caster registration:

```java
MapContext context = new MapContext(Map.of());
Engine engine = Engine.withData(context);

// Register a custom caster
context.registerCaster(Integer.class, Boolean.class, i -> i != 0);

// Now integers can be used as booleans in operations
Script script = engine.getOp("if").args(1, 2, 3);
Object result = script.evaluate();
// result = 2 (because 1 != 0 is truthy)
```

#### Proxy Registration

Register proxies to enable map-like or list-like access to custom objects:

```java
MapContext context = new MapContext(Map.of("a", myCustomObject));
context.registerProxy(MyCustomClass.class, new MyCustomProxyFactory());
Engine engine = Engine.withData(context);

// Now custom objects can be accessed like maps
Script script = engine.getOp("var").args("a.field");
Object result = script.evaluate();
```

#### Java Reflection Access

MapContext provides a convenience method for Java reflection-based field access:

```java
public class MyClass {
    public String testField = "Hello, World!";
}

MyClass obj = new MyClass();
MapContext context = new MapContext(Map.of("a", obj));
context.convenience.doJavaIntrospection();
Engine engine = Engine.withData(context);

// Access fields via reflection
Script script = engine.getOp("var").args("a.testField");
Object result = script.evaluate();
// result = "Hello, World!"
```

### Operation Management

#### Registering Operations

Operations are automatically loaded via ServiceLoader, but you can also register them programmatically:

```java
Engine engine = Engine.withData(Map.of());

// Register a new operation
engine.registerOperation(new MyCustomOperation());

// Update an existing operation
engine.updateOperation(new OverrideOperation());

// Register or update (no exception if already exists)
engine.registerOrUpdateOperation(new MyOperation());
```

#### Accessing Operations

```java
Engine engine = Engine.withData(Map.of());

// Get an operation by symbol
Optional<Operation> op = engine.getOperation("log");
if (op.isPresent()) {
    Operation operation = op.get();
    // Use the operation
}
```

### Script Node Types

The engine creates different types of script nodes:

- **FunctionNode**: For `Operation.Function` - arguments are evaluated before the function is called
- **MacroNode**: For `Operation.Macro` - receives unevaluated arguments and can control evaluation
- **ConstantValueNode**: For constant values in scripts

## Key Path Access Patterns

MapContext supports flexible key access patterns:

- **Simple key**: `"key"` - Access a top-level map key
- **Nested key**: `"key.subkey"` - Access nested map values
- **Array index**: `"key[0]"` - Access array/list elements by index
- **Mixed**: `"key.subkey[0].field"` - Combine nested access and indexing
- **Empty key**: `""` - Returns the entire context data object

## Type Casting

MapContext includes built-in casters for:

- **Numeric types**: Integer, Long, Short, Byte, Float, Double, BigInteger, BigDecimal
- **Primitive to wrapper**: Automatic conversion between primitives and their wrapper types
- **String**: Default string conversion for any type
- **Custom casters**: Register your own type converters

## Proxy System

The proxy system allows you to extend MapContext to work with custom object types:

- **MappedProxyFactory**: For objects that should be accessed like maps (key-value pairs)
- **IndexedProxyFactory**: For objects that should be accessed like arrays/lists (indexed access)

Proxies are registered by class/interface and are automatically used when accessing objects of that type.

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
- Main project README for overall project information


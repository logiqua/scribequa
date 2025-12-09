# Logiqua API

The Logiqua API module defines the core public interfaces that all Logiqua implementations must follow. This module provides the foundation for building expression evaluation engines with support for operations, context management, and script execution.

## Overview

The `api` module contains the public interfaces that define the contract for Logiqua implementations. Different implementations (XML, YAML, JSON, etc.) all implement these interfaces, providing a consistent API regardless of the input format.

## Features

- **Unified API**: Common interfaces for all Logiqua implementations
- **Operation System**: Extensible operation framework supporting both commands and functions
- **Context Management**: Flexible context system for variable access and data handling
- **Type Safety**: Type information and return type tracking
- **Extensibility**: Service loader pattern for automatic operation discovery

## Architecture

### Core Interfaces

#### `Logiqua`
The main entry point interface for compiling scripts:

```java
public interface Logiqua {
    Script compile(String script);
}
```

Implementations of this interface parse a script string and return a `Script` object that can be evaluated.

#### `Script`
Represents the Abstract Syntax Tree (AST) of a calculation:

```java
public interface Script {
    Object evaluate();
    Object evaluateUsing(Executor executor);
    Set<Class<?>> returns();
    String jsonify();
}
```

A `Script` contains an operation and its arguments. Each argument is itself a `Script` that can be evaluated. The script can evaluate itself or use a specific executor for evaluation with local context.

The `jsonify()` method converts the script to its JSON string representation, regardless of the original format used to compile it. This enables canonical storage and JSON-based searchability of scripts.

#### `Executor`
Orchestrates script execution and provides access to operations:

```java
public interface Executor {
    Context getContext();
    Operation getOperation(String symbol);
    void updateOperation(Operation operation);
    void registerOperation(Operation operation);
    void registerOrUpdateOperation(Operation operation);
}
```

The executor manages the execution context and provides access to operations by their symbol names.

#### `Context`
Provides access to variables and data during script execution:

```java
public interface Context {
    Value get(String key);
    Accessor accessor(Object target);
    <From, To> Optional<Caster<From, To>> caster(Class<From> from, Class<To> to);
}
```

The context supports:
- **Key-value access**: Retrieve values by string keys
- **Collection access**: Access map-like and list-like structures via `Accessor`
- **Type casting**: Convert between types using `Caster` functions

#### `Operation`
Defines operations that can be performed on values:

```java
public sealed interface Operation extends Named 
    permits Operation.Function, Operation.Command {
    
    int[] argLimits();
    void checkArguments(Script... args);
    Set<Class<?>> returns(Script... args);
    
    non-sealed interface Function extends Operation {
        Object evaluate(Executor executor, Object... args);
    }
    
    non-sealed interface Command extends Operation {
        Object evaluate(Executor executor, Script... args);
    }
}
```

Operations come in two flavors:
- **Function**: Arguments are evaluated first, then the function is called
- **Command**: Receives unevaluated arguments and can evaluate them at its discretion

#### `Named`
Interface for entities that have a symbol name:

```java
public interface Named {
    String symbol();
    
    @interface Symbol {
        String value();
    }
}
```

Operations implement this interface to provide their symbol name, which can be specified via annotation or derived from the class name.

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>api</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage Pattern

The typical usage pattern involves:

1. **Compile a script** using a `Logiqua` implementation:
   ```java
   Logiqua logiqua = new XmlLogiqua(); // or YamlLogiqua(), JsonLogiqua(), JsonLogicLogiqua(), LspLogiqua(), etc.
   Script script = logiqua.compile(scriptString);
   ```

2. **Evaluate the script**:
   ```java
   Object result = script.evaluate();
   ```

3. **Evaluate with context** (if needed):
   ```java
   Map<String, Object> contextData = Map.of("x", 10, "y", 20);
   Logiqua logiqua = new XmlLogiqua().with(contextData);
   Script script = logiqua.compile(scriptString);
   Object result = script.evaluate();
   ```

4. **Convert script to JSON format** (for canonical storage):
   ```java
   // Compile script from any format (YAML, XML, LISP, etc.)
   Script script = yamlLogiqua.compile(yamlScript);
   
   // Convert to JSON format for storage
   String jsonRepresentation = script.jsonify();
   // Store jsonRepresentation in database or search using JSON tools
   ```

### Creating Custom Operations

#### Creating a Function

Functions receive pre-evaluated arguments:

```java
@Named.Symbol("+")
public class AddFunction implements Operation.Function {
    
    @Override
    public Object evaluate(Executor executor, Object... args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Add requires at least 2 arguments");
        }
        
        Number sum = 0;
        for (Object arg : args) {
            if (arg instanceof Number num) {
                sum = sum.doubleValue() + num.doubleValue();
            } else {
                throw new IllegalArgumentException("Add requires numeric arguments");
            }
        }
        return sum;
    }
    
    @Override
    public int[] argLimits() {
        return new int[]{2, -1}; // At least 2, no maximum
    }
    
    @Override
    public Set<Class<?>> returns(Script... args) {
        return Set.of(Number.class);
    }
}
```

#### Creating a Command

Commands receive unevaluated `Script` arguments and can control evaluation:

```java
@Named.Symbol("if")
public class IfCommand implements Operation.Command {
    
    @Override
    public Object evaluate(Executor executor, Script... args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("If requires exactly 3 arguments");
        }
        
        Object condition = args[0].evaluateUsing(executor);
        if (isTruthy(condition)) {
            return args[1].evaluateUsing(executor);
        } else {
            return args[2].evaluateUsing(executor);
        }
    }
    
    @Override
    public int[] argLimits() {
        return new int[]{3, 3}; // Exactly 3 arguments
    }
    
    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean b) return b;
        if (value instanceof Number n) return n.doubleValue() != 0;
        return true;
    }
}
```

#### Registering Operations

Operations are automatically discovered via Java's ServiceLoader mechanism:

1. Create a file `META-INF/services/com.javax0.logiqua.Operation`
2. Add the fully qualified class name of your operation

Alternatively, you can register operations programmatically:

```java
Executor executor = ...; // Get executor from implementation
executor.registerOperation(new MyCustomOperation());
```

### Working with Context

#### Accessing Variables

The context provides access to variables by key:

```java
Context context = executor.getContext();
Context.Value value = context.get("variableName");
if (value != null) {
    Object data = value.get();
    // Use the data
}
```

#### Collection Access

The context can provide accessors for collection-like objects:

```java
Context context = executor.getContext();
Context.Accessor accessor = context.accessor(myCollection);

if (accessor instanceof Context.Mapped mapped) {
    // Access as map-like structure
    Context.Value value = mapped.get("key");
} else if (accessor instanceof Context.Indexed indexed) {
    // Access as list-like structure
    Context.Value value = indexed.get(0);
    int size = indexed.size();
}
```

#### Type Casting

The context can provide casters for type conversion:

```java
Context context = executor.getContext();
Optional<Context.Caster<Integer, String>> caster = 
    context.caster(Integer.class, String.class);

if (caster.isPresent()) {
    String result = caster.get().cast(123);
}
```

## Operation Argument Limits

Operations can specify argument limits using the `@Limited` annotation:

```java
@Named.Symbol("min")
@Operation.Limited(min = 1, max = -1) // At least 1, no maximum
public class MinFunction implements Operation.Function {
    // ...
}
```

Or by implementing `argLimits()`:

```java
@Override
public int[] argLimits() {
    return new int[]{1, -1}; // [min, max], -1 means no limit
}
```

## Return Type Information

Operations can specify their return types:

```java
@Override
public Set<Class<?>> returns(Script... args) {
    // Return the union of argument types
    Set<Class<?>> types = new HashSet<>();
    for (Script arg : args) {
        types.addAll(arg.returns());
    }
    return types;
}
```

## Implementation Modules

The API module is implemented by:

- **xml**: XML-based script format
- **yaml**: YAML-based script format
- **json**: JSON-based script format
- **jsonlogic**: JSONLogic-compatible format
- **lsp**: LISP-based script format

All implementations provide the same `Logiqua` interface, allowing you to switch between formats seamlessly.

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
- JUnit Jupiter (for testing)

## License

See the main project LICENSE file for license information.

## See Also

- [API Documentation](target/reports/apidocs/index.html) - Full API reference
- Main project README for overall project information
- Implementation modules (xml, yaml, json, jsonlogic, lsp) for usage examples


# Logiqua vs json-logic-java Comparison

This document provides a comprehensive comparison between **Logiqua** and **[json-logic-java](https://github.com/jamsesso/json-logic-java)**, two Java implementations for evaluating JsonLogic expressions.

## Overview

### json-logic-java

[json-logic-java](https://github.com/jamsesso/json-logic-java) is a pure Java implementation of JsonLogic that attempts to mimic the public API of the original JavaScript implementation. It was created to provide JsonLogic functionality without relying on the Nashorn JavaScript engine.

**Key Characteristics:**
- Pure Java implementation (no JavaScript engine)
- Direct JsonLogic compatibility
- Simple, straightforward API
- MIT License
- Version 1.1.0

### Logiqua

**Logiqua** is a modular, extensible expression evaluation engine that supports multiple formats (JSON, YAML, XML, LISP-style) and provides JsonLogic compatibility as one of its features.

**Key Characteristics:**
- Multi-format support (JSON, YAML, XML, LISP)
- Modular architecture with separate API, Engine, and Commands modules
- Extensible operation system via ServiceLoader
- Advanced context management and type coercion
- Apache 2.0 License
- Version 1.0.0

## Architecture Comparison

### json-logic-java

```
json-logic-java
└── JsonLogic (single class)
    ├── Operation registry
    ├── Expression parser
    └── Evaluator
```

**Architecture:**
- **Focused**: Single `JsonLogic` class handles parsing, operation management, and evaluation
- **Simple**: Straightforward implementation focused on JsonLogic compatibility
- **Self-contained**: All functionality in one module
- **Lightweight**: Minimal dependencies and overhead

### Logiqua

```
Logiqua
├── api/              # Core interfaces (Logiqua, Script, Executor, Context, Operation)
├── engine/           # Execution engine with context management
├── commands/         # Standard operations library
├── json/             # JSON format parser
├── yaml/             # YAML format parser
├── xml/              # XML format parser
├── lsp/              # LISP-style format parser
└── jsonlogic/        # JsonLogic compatibility layer
```

**Architecture:**
- **Modular**: Separated concerns with dedicated modules for different responsibilities
- **Extensible**: ServiceLoader-based operation discovery
- **Flexible**: Multiple format parsers sharing the same execution engine

## API Comparison

### json-logic-java API

```java
// Create instance
JsonLogic jsonLogic = new JsonLogic();

// Evaluate expression
String expression = "{\"*\": [{\"var\": \"x\"}, 2]}";
Map<String, Integer> data = new HashMap<>();
data.put("x", 10);
Object result = jsonLogic.apply(expression, data);

// Add custom operation
jsonLogic.addOperation("greet", (args) -> "Hello, " + args[0] + "!");

// Truthy check
boolean isTruthy = JsonLogic.truthy(value);
```

**Characteristics:**
- **Simple API**: Single class with straightforward methods - easy to learn and use
- **String-based**: Expressions passed as JSON strings - no compilation step needed
- **Duck-typing**: Relies on duck-typing for maps and arrays - flexible data handling
- **Thread-safe**: JsonLogic instances are thread-safe
- **Direct compatibility**: Matches JavaScript JsonLogic API closely

### Logiqua API

```java
// Create instance (multiple format options)
JsonLogiqua json = new JsonLogiqua().with(data);
YamlLogiqua yaml = new YamlLogiqua().with(data);
XmlLogiqua xml = new XmlLogiqua().with(data);
LspLogiqua lsp = new LspLogiqua().with(data);

// Compile and evaluate
Script script = json.compile(expression);
Object result = script.evaluate();

// Or use JsonLogic compatibility
JsonLogic jsonLogic = new JsonLogic();
Object result = jsonLogic.apply(jsonString, data);

// Add custom operation (via ServiceLoader or programmatically)
engine.registerOperation(new MyCustomOperation());
```

**Characteristics:**
- **Multi-format**: Different classes for different formats
- **Compiled Scripts**: Expressions compiled to `Script` objects for reuse
- **Type-safe**: Strong typing with interfaces and generics
- **Context-aware**: Advanced context management with nested access

## Feature Comparison

| Feature | json-logic-java | Logiqua |
|---------|----------------|---------|
| **JsonLogic Compatibility** | ✅ Full compatibility | ✅ Full compatibility (via `jsonlogic` module) |
| **Format Support** | ✅ JSON (focused on JsonLogic) | JSON, YAML, XML, LISP-style |
| **Comments Support** | ❌ (JSON doesn't support comments) | ✅ (YAML, LISP, XML) |
| **Operation Discovery** | ✅ Simple `addOperation()` method | ServiceLoader automatic discovery |
| **Custom Operations** | ✅ Simple `addOperation()` method | ServiceLoader or programmatic |
| **Type Coercion** | ✅ Duck-typing (flexible, JavaScript-like) | Advanced with registered casters |
| **Context Management** | ✅ Simple map-based (easy to use) | Advanced with nested access, proxies |
| **Script Compilation** | ✅ Direct evaluation (no compilation overhead) | Compiles to reusable `Script` objects |
| **Thread Safety** | ✅ Thread-safe instances | ✅ Thread-safe (immutable scripts) |
| **Error Handling** | ✅ Standard Java exceptions | Detailed error messages with paths |
| **Array Operations** | ✅ Full JsonLogic standard support | Enhanced with local context variables |
| **Variable Access** | ✅ Standard JsonLogic `var` operation | Advanced nested paths (`user.name`, `items[0]`) |
| **Type Information** | ❌ (not needed for JsonLogic) | Return type tracking |
| **Extensibility** | ✅ Simple operation registration | Highly extensible architecture |
| **Compiled Script Caching** | ✅ Internal caching (automatic, no setup) | Application-managed caching via compiled `Script` |
| **Dependencies** | ✅ Minimal (no external dependencies) | Multiple modules and dependencies |
| **Learning Curve** | ✅ Very simple (single class) | Steeper (multiple modules to understand) |
| **Maturity** | ✅ More mature (v1.1.0, proven track record) | Newer (v1.0.0) |



## Use Cases

### When to Use json-logic-java

**Best For:**
- ✅ JsonLogic evaluation (primary use case)
- ✅ Direct drop-in replacement for JavaScript JsonLogic
- ✅ Minimal dependencies and simple integration
- ✅ Projects that only need JSON format (most common case)
- ✅ Quick prototyping and development
- ✅ Simple, straightforward API requirements
- ✅ When you want proven, mature library
- ✅ When simplicity and ease of use are priorities

**Example Use Case:**
```java
// Simple rule evaluation
JsonLogic jsonLogic = new JsonLogic();
String rule = "{\"and\": [{\">\": [{\"var\": \"age\"}, 18]}, {\"==\": [{\"var\": \"country\"}, \"US\"]}]}";
Map<String, Object> data = Map.of("age", 25, "country", "US");
boolean result = (boolean) jsonLogic.apply(rule, data);
```

### When to Use Logiqua

**Best For:**
- ✅ Multi-format expression evaluation
- ✅ Complex rule engines requiring extensibility
- ✅ Projects needing advanced context management
- ✅ Integration with XML/YAML-based systems
- ✅ Custom operation development
- ✅ Type-safe expression evaluation
- ✅ Script compilation and reuse

**Example Use Case:**
```java
// Multi-format support
YamlLogiqua yaml = new YamlLogiqua().with(data);
String rule = """
    if:
      - and:
        - ">":
          - var: "age"
          - 18
        - "==":
          - var: "country"
          - "US"
      - "Eligible"
      - "Not eligible"
    """;
Script script = yaml.compile(rule);
Object result = script.evaluate(); // Can reuse script multiple times
```

## Migration Guide

### Migrating from json-logic-java to Logiqua

#### 1. Dependency Change

**json-logic-java:**
```xml
<dependency>
    <groupId>io.github.jamsesso</groupId>
    <artifactId>json-logic-java</artifactId>
    <version>1.1.0</version>
</dependency>
```

**Logiqua:**
```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>jsonlogic</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 2. API Changes

**json-logic-java:**
```java
JsonLogic jsonLogic = new JsonLogic();
Object result = jsonLogic.apply(jsonString, data);
```

**Logiqua (JsonLogic compatibility):**
```java
JsonLogic jsonLogic = new JsonLogic();
Object result = jsonLogic.apply(jsonString, data);
// Same API! Drop-in replacement
```

**Logiqua (Native API):**
```java
JsonLogiqua json = new JsonLogiqua().with(data);
Script script = json.compile(jsonString);
Object result = script.evaluate(); // Can reuse script
```

#### 3. Custom Operations

**json-logic-java:**
```java
jsonLogic.addOperation("greet", (args) -> "Hello, " + args[0] + "!");
```

**Logiqua:**
```java
// Option 1: Programmatic registration
engine.registerOperation(new MyCustomOperation());

// Option 2: ServiceLoader (automatic discovery)
// Create META-INF/services/com.javax0.logiqua.Operation$Function
```

#### 4. Truthy Checks

**json-logic-java:**
```java
boolean isTruthy = JsonLogic.truthy(value);
```

**Logiqua:**
```java
boolean isTruthy = JsonLogic.truthy(value);
// Same static method available
```

## Performance Considerations

### json-logic-java
- **Parsing**: Parses JSON string on each evaluation (simple, no compilation overhead)
- **Memory**: ✅ Lower memory footprint (single class, minimal overhead)
- **Startup**: ✅ Faster startup (no module loading, no ServiceLoader discovery)
- **Evaluation**: ✅ Direct evaluation without compilation step (faster for one-off evaluations)
- **Caching**: Internal caching via ConcurrentHashMap (automatic, no configuration)

### Logiqua
- **Parsing**: Compiles to `Script` objects (can be reused, but compilation overhead)
- **Memory**: Higher memory footprint (modular architecture, multiple classes)
- **Startup**: Slower startup (ServiceLoader operation discovery, module loading)
- **Evaluation**: Compiled scripts can be cached and reused (better for repeated evaluations)
- **Caching**: Application-managed caching (more control, but requires setup)

**Performance Recommendation:**
- **json-logic-java**: ✅ Better for one-off evaluations, simple use cases, and when startup time matters
- **Logiqua**: Better for repeated evaluations of the same expression (compile once, evaluate many times)

## Pros and Cons

### json-logic-java

**Pros:**
- ✅ **Simple, straightforward API** - Single class, easy to learn and use
- ✅ **Direct JsonLogic compatibility** - Matches JavaScript implementation closely
- ✅ **Minimal dependencies** - No external dependencies, lightweight
- ✅ **Fast startup** - No module loading or ServiceLoader discovery
- ✅ **Lower memory footprint** - Single class, minimal overhead
- ✅ **Thread-safe** - Instances are thread-safe
- ✅ **Mature and proven** - Version 1.1.0, established track record
- ✅ **Internal caching** - Automatic caching via ConcurrentHashMap
- ✅ **Easy integration** - Drop-in replacement for JavaScript JsonLogic

**Cons:**
- ❌ JSON format only
- ❌ No script compilation/reuse
- ❌ Limited extensibility
- ❌ Basic type handling
- ❌ No comments support
- ❌ Parses JSON on every evaluation

### Logiqua

**Pros:**
- ✅ Multiple format support (JSON, YAML, XML, LISP) - flexibility for different use cases
- ✅ Script compilation and reuse - better performance for repeated evaluations
- ✅ Highly extensible architecture - ServiceLoader-based operation discovery
- ✅ Advanced type coercion - registered casters for type conversion
- ✅ Comments support (YAML, LISP, XML) - better documentation in expressions
- ✅ Advanced context management - nested paths, proxies for custom objects
- ✅ Type information tracking - return type information for operations
- ✅ Better error messages - detailed error messages with paths

**Cons:**
- ❌ **More complex architecture** - Multiple modules to understand
- ❌ **Higher memory footprint** - Modular architecture requires more memory
- ❌ **Slower startup** - ServiceLoader operation discovery adds startup time
- ❌ **More dependencies** - Multiple modules and external dependencies
- ❌ **Steeper learning curve** - More concepts to learn (API, Engine, Script, etc.)
- ❌ **Overkill for simple use cases** - May be more than needed for basic JsonLogic evaluation

## Compatibility

### JsonLogic Specification

Both implementations are compatible with the JsonLogic specification:

- ✅ **json-logic-java**: Direct compatibility (primary goal)
- ✅ **Logiqua**: Full compatibility via `jsonlogic` module

### Expression Compatibility

Expressions written for json-logic-java will work with Logiqua's `jsonlogic` module without modification:

```java
// Works in both
String expression = "{\"and\": [{\"var\": \"x\"}, {\"var\": \"y\"}]}";

// json-logic-java
JsonLogic jsonLogic = new JsonLogic();
Object result = jsonLogic.apply(expression, data);

// Logiqua
JsonLogic jsonLogic = new JsonLogic();
Object result = jsonLogic.apply(expression, data);
```

## Recommendations

### Choose json-logic-java if:
- ✅ You need JsonLogic functionality (most common case)
- ✅ You prefer a simple, single-class API
- ✅ You need minimal dependencies and lightweight solution
- ✅ You're doing one-off or occasional evaluations
- ✅ You want a direct JavaScript JsonLogic port
- ✅ You value simplicity and ease of use
- ✅ You want a mature (Java 8), proven library
- ✅ You need fast startup times
- ✅ You want automatic internal caching without configuration

**In summary**: Choose json-logic-java when you need a simple, focused JsonLogic implementation that "just works" without complexity.

### Choose Logiqua if:
- ✅ You need multiple format support (YAML, XML, LISP in addition to JSON)
- ✅ You want to compile and reuse scripts (better performance for repeated evaluations)
- ✅ You need advanced extensibility (ServiceLoader-based operation discovery)
- ✅ You require advanced context management (nested paths, custom object proxies)
- ✅ You want type-safe expression evaluation with return type tracking
- ✅ You need comments in your expressions (YAML, LISP, XML formats)
- ✅ You're building a complex rule engine with custom operations
- ✅ You need integration with XML/YAML-based systems

**In summary**: Choose Logiqua when you need more than basic JsonLogic - multi-format support, advanced features, or extensibility.

### Decision Matrix

| Your Need | Recommended Library |
|-----------|-------------------|
| Simple JsonLogic evaluation | **json-logic-java** |
| Minimal dependencies | **json-logic-java** |
| Fast startup | **json-logic-java** |
| Simple API | **json-logic-java** |
| Multiple formats (YAML, XML, LISP) | **Logiqua** |
| Script compilation and reuse | **Logiqua** |
| Advanced extensibility | **Logiqua** |
| Complex rule engine | **Logiqua** |
| Comments in expressions | **Logiqua** |

## Conclusion

Both libraries provide JsonLogic compatibility, but serve different needs:

- **json-logic-java** is a focused, simple, and lightweight solution for JsonLogic evaluation. It excels at doing one thing well: evaluating JsonLogic expressions with minimal overhead and maximum simplicity.

- **Logiqua** is a comprehensive, extensible expression evaluation framework that supports multiple formats and advanced features. It's designed for complex use cases that require more than basic JsonLogic evaluation.

**The choice depends on your needs:**

- **For most JsonLogic use cases**: **json-logic-java** is the better choice - it's simpler, lighter, faster to start, and has a proven track record. It does exactly what you need without unnecessary complexity.

- **For advanced use cases**: **Logiqua** is the better choice - when you need multi-format support, script compilation, advanced extensibility, or complex rule engines.

Neither library is "better" overall - they're optimized for different use cases. Choose based on your specific requirements: simplicity and focus (json-logic-java) vs. flexibility and extensibility (Logiqua).

## References

- **json-logic-java**: [https://github.com/jamsesso/json-logic-java](https://github.com/jamsesso/json-logic-java)
- **Logiqua**: [https://github.com/logiqua/scribequa](https://github.com/logiqua/scribequa)
- **JsonLogic Specification**: [http://jsonlogic.com/](http://jsonlogic.com/)


# Logiqua

**Logiqua** is a powerful, extensible expression evaluation engine for Java. It provides a unified API for evaluating expressions written in multiple formats (JSON, YAML, XML, LISP-style, or infix expressions), making it easy to build rule engines, configuration systems, and dynamic expression evaluators.

## Overview

Logiqua is a modular expression evaluation framework that allows you to:

- **Write expressions in multiple formats**: JSON, YAML, XML, LISP-style syntax, or infix expressions
- **Evaluate complex logic**: Support for arithmetic, comparisons, conditionals, and array operations
- **Access variables**: Flexible context system for variable access and data binding
- **Extend functionality**: Easy to add custom operations via the ServiceLoader mechanism
- **Type-safe evaluation**: Automatic type coercion and type information tracking
- **Canonical JSON storage**: Convert any script to JSON format regardless of original format for standardized storage and searchability

## Features

- **Multiple Format Support**: Write expressions in JSON, YAML, XML, LISP-style syntax, or infix expressions
- **JsonLogic Compatible**: Full compatibility with JsonLogic specification
- **Extensible Operations**: Easy to add custom operations via ServiceLoader
- **Type Coercion**: Automatic type conversion between compatible types
- **Context Management**: Flexible context system for variable access
- **Array Operations**: Built-in support for filtering, mapping, reducing arrays
- **Comments Support**: YAML, LISP, XML, and Expression formats support comments for documentation
- **Canonical JSON Conversion**: Convert any script to JSON format using `jsonify()` for standardized storage and JSON-based searchability
- **Well-Tested**: Comprehensive test coverage across all modules

## Quick Start

### Maven Dependency

Add the format-specific module you want to use to your `pom.xml`:

```xml
<!-- For JSON format -->
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>json</artifactId>
    <version>2.0.1</version>
</dependency>

<!-- For YAML format -->
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>yaml</artifactId>
    <version>2.0.1</version>
</dependency>

<!-- For XML format -->
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>xml</artifactId>
    <version>2.0.1</version>
</dependency>

<!-- For LISP-style format -->
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>lsp</artifactId>
    <version>2.0.1</version>
</dependency>

<!-- For infix expression format -->
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>exp</artifactId>
    <version>2.0.1</version>
</dependency>

<!-- For JsonLogic compatibility -->
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>jsonlogic</artifactId>
    <version>2.0.1</version>
</dependency>
```

You can use more than one Logiqua module in the same project. For example, you might use JSON for certain applications, YAML for ease of editing, and XML for integration with tools that generate XML—or any combination thereof. Each module can be instantiated separately, and you can freely combine them as needed in your codebase.

### Basic Example

#### JSON Format

```java
import com.javax0.logiqua.json.JsonLogiqua;
import com.javax0.logiqua.Script;
import java.util.Map;

JsonLogiqua json = new JsonLogiqua().with(Map.of("x", 10, "y", 20));
Script script = json.compile("""
    {
        "+": [
            {"var": "x"},
            {"var": "y"}
        ]
    }
    """);
Object result = script.evaluate();
// result = 30
```

#### YAML Format

```java
import com.javax0.logiqua.yaml.YamlLogiqua;
import com.javax0.logiqua.Script;
import java.util.Map;

YamlLogiqua yaml = new YamlLogiqua().with(Map.of("x", 10, "y", 20));
Script script = yaml.compile("""
    "+":
      - var: "x"
      - var: "y"
    """);
Object result = script.evaluate();
// result = 30
```

#### LISP-style Format

```java
import com.javax0.logiqua.lsp.LspLogiqua;
import com.javax0.logiqua.Script;
import java.util.Map;

LspLogiqua lsp = new LspLogiqua().with(Map.of("x", 10, "y", 20));
Script script = lsp.compile("(+ (var \"x\") (var \"y\"))");
Object result = script.evaluate();
// result = 30
```

#### XML Format

```java
import com.javax0.logiqua.xml.XmlLogiqua;
import com.javax0.logiqua.Script;
import java.util.Map;

XmlLogiqua xml = new XmlLogiqua().with(Map.of("x", 10, "y", 20));
Script script = xml.compile("""
    <op symbol="+">
        <var id="x"/>
        <var id="y"/>
    </op>
    """);
Object result = script.evaluate();
// result = 30
```

#### Infix Expression Format

```java
import com.javax0.logiqua.exp.ExpLogiqua;
import com.javax0.logiqua.Script;
import java.util.Map;

ExpLogiqua exp = new ExpLogiqua().with(Map.of("x", 10, "y", 20));
Script script = exp.compile("x + y");
Object result = script.evaluate();
// result = 30
```

### Converting Scripts to JSON Format

The `jsonify()` method allows you to convert any script to JSON format, regardless of its original format. This is useful for storing scripts in a canonical format and enabling JSON-based searchability:

```java
import com.javax0.logiqua.yaml.YamlLogiqua;
import com.javax0.logiqua.Script;
import java.util.Map;

// Compile a script from YAML format
YamlLogiqua yaml = new YamlLogiqua().with(Map.of("x", 10, "y", 20));
Script script = yaml.compile("""
    "+":
      - var: "x"
      - var: "y"
    """);

// Convert to JSON format for storage
String jsonRepresentation = script.jsonify();
// jsonRepresentation = "{\"+\":[{\"var\":[\"x\"]},{\"var\":[\"y\"]}]}"

// The JSON can be stored in a database or searched using JSON tools
// Later, you can recompile from JSON if needed
```

This feature enables applications to:
- **Store scripts in a canonical format**: Regardless of whether scripts were originally written in YAML, XML, LISP, or infix expressions, they can all be stored as JSON
- **Enable JSON-based searchability**: Use JSON query tools to search and filter scripts by their structure
- **Maintain format independence**: Store scripts in a format-independent way while preserving the ability to work with them in any format

## Architecture

Logiqua is built as a modular system with the following components:

### Core Modules

- **`api`**: Defines the core interfaces (`Logiqua`, `Script`, `Executor`, `Context`, `Operation`)
- **`engine`**: Provides the execution engine with operation management and context handling
- **`commands`**: Contains the standard library of operations (arithmetic, logic, arrays, strings, etc.)

### Format Modules

- **`json`**: JSON-based expression parser
- **`yaml`**: YAML-based expression parser (JsonLogic compatible)
- **`xml`**: XML-based expression parser
- **`lsp`**: LISP-style expression parser
- **`exp`**: Infix expression parser with operator precedence

### Compatibility Modules

- **`jsonlogic`**: JsonLogic compatibility layer

### Utility Modules

- **`lex`**: Lexical analysis framework used by the LSP and Expression modules

### Application Modules

- **`fx`**: Interactive GUI playground application built with JavaFX for testing and experimenting with Logiqua expressions

## Project Structure

```
Logiqua/
├── api/              # Core API interfaces
├── engine/           # Execution engine
├── commands/         # Standard operations library
├── json/             # JSON format parser
├── yaml/             # YAML format parser
├── xml/              # XML format parser
├── lsp/              # LISP-style format parser
├── exp/              # Infix expression format parser
├── jsonlogic/        # JsonLogic compatibility
├── lex/              # Lexical analysis framework
└── fx/               # Interactive GUI playground application
```

## Module Documentation

Each module has its own comprehensive README:

- **[API Module](api/README.md)** - Core interfaces and contracts
- **[Engine Module](engine/README.md)** - Execution engine and context management
- **[Commands Module](commands/README.md)** - Standard operations library
- **[JSON Module](json/README.md)** - JSON format parser
- **[YAML Module](yaml/README.md)** - YAML format parser
- **[XML Module](xml/README.md)** - XML format parser
- **[LSP Module](lsp/README.md)** - LISP-style format parser
- **[Expression Module](exp/README.md)** - Infix expression format parser
- **[JsonLogic Module](jsonlogic/README.md)** - JsonLogic compatibility
- **[Lex Module](lex/README.md)** - Lexical analysis framework
- **[FX Module](fx/README.md)** - Interactive GUI playground application

## Supported Operations

Logiqua provides a comprehensive set of built-in operations:

### Logic Operations
- `and`, `or`, `not`, `if` - Boolean logic and conditionals

### Comparison Operations
- `==`, `!=`, `<`, `<=`, `>`, `>=`, `between` - Value comparisons

### Mathematical Operations
- `+`, `-`, `*`, `/`, `%` - Arithmetic operations
- `min`, `max` - Min/max values

### Variable Access
- `var` - Access variables from context with support for nested paths

### Array Operations
- `filter`, `map`, `reduce` - Array transformations
- `all`, `some`, `none` - Array quantifiers
- `in` - Membership testing

### String Operations
- `cat` - String concatenation
- `substr` - Substring extraction

### Utility Operations
- `log` - Logging
- `missing`, `missingSome` - Missing key detection
- `merge` - Array merging
- `selectOne` - First non-null value

See the [Commands Module README](commands/README.md) for complete documentation of all operations.

## Format Comparison

| Feature | JSON | YAML | XML | LISP | Expression |
|---------|------|------|-----|------|------------|
| Comments | ❌ | ✅ | ✅ | ✅ | ✅ |
| Human-readable | ⚠️ | ✅ | ⚠️ | ✅ | ✅ |
| JsonLogic Compatible | ✅ | ❌ | ❌ | ❌ | ❌ |
| Infix Notation | ❌ | ❌ | ❌ | ❌ | ✅ |
| Operator Precedence | ❌ | ❌ | ❌ | ❌ | ✅ |
| Nested Structures | ✅ | ✅ | ✅ | ✅ | ✅ |
| Type Preservation | ✅ | ✅ | ✅ | ✅ | ✅ |

## Building

The project is built using Maven:

```bash
mvn clean install
```

To build all modules:

```bash
mvn clean install -DskipTests
```

## Testing

Run all tests:

```bash
mvn test
```

Run tests for a specific module:

```bash
cd <module>
mvn test
```

## Interactive Playground

Logiqua includes an interactive GUI playground application (`fx` module) built with JavaFX that allows you to:

- **Test expressions interactively**: Enter expressions in any supported format (JSON, YAML, XML, LISP, or infix expressions)
- **Provide context data**: Enter data in JSON format to test expressions with variables
- **See results instantly**: View evaluation results or error messages in real-time
- **Switch formats easily**: Select different format parsers with radio buttons

### Running the GUI Playground

To run the interactive playground:

```bash
cd fx
mvn javafx:run
```

Or build and run the JAR:

```bash
cd fx
mvn clean package
java -jar target/fx-1.0.1.jar
```

The GUI provides a simple interface where you can:
1. Select the format (JSON, YAML, XML, LISP, Expression, or JSON Compatibility Mode)
2. Enter your expression in the "Logic" text area
3. Optionally enter data in JSON format in the "Data" text area
4. Click "Run" to evaluate the expression
5. View the result or any error messages in the "Result" text area

See the [FX Module README](fx/README.md) for more details and a screenshot.

## Requirements

- **Java 21+**: Logiqua requires Java 21 or higher
- **Maven 3.6+**: For building the project
- **JavaFX** (optional): Required only for the `fx` GUI playground module

## License

Logiqua is dual-licensed under both the [MIT License](LICENSES) and the [Apache License, Version 2.0](LICENSES). You may choose to use this software under either license, whichever better fits your needs.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Links

- **GitHub**: [https://github.com/logiqua/scribequa](https://github.com/logiqua/scribequa)
- **Issues**: [https://github.com/logiqua/scribequa/issues](https://github.com/logiqua/scribequa/issues)

## Author

**Peter Verhas** - [peter@verhas.com](mailto:peter@verhas.com)

## See Also

- [JsonLogic](http://jsonlogic.com/) - JsonLogic specification
- [SnakeYAML](https://bitbucket.org/asomov/snakeyaml) - YAML parser used by the YAML module

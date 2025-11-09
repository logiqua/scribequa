# Logiqua XML

The XML module provides an XML-based syntax parser for Logiqua. It allows you to write Logiqua expressions using XML format, making it suitable for integration with XML-based systems and tools.

## Overview

The `xml` module implements the `Logiqua` interface, parsing XML-formatted expressions and converting them into Logiqua scripts. It uses standard Java DOM parsing and provides a structured, hierarchical syntax for writing expressions.

## Features

- **XML Syntax**: Structured, hierarchical XML format
- **Standard DOM Parsing**: Uses Java's built-in XML parsing
- **Automatic Operation Discovery**: Uses all standard Logiqua operations
- **Context Support**: Full support for variable access and data binding
- **Type Preservation**: Explicit type specification for constants
- **Well-Formed XML**: Requires valid XML syntax

## Architecture

### Main Class

#### `XmlLogiqua`
The main entry point for compiling XML-style scripts:

```java
public class XmlLogiqua implements Logiqua {
    public Engine engine();
    public XmlLogiqua with(Engine engine);
    public XmlLogiqua with(Map<String, Object> data);
    public Script compile(String source);
}
```

### Components

- **XmlReader**: Parses XML strings into DOM documents using Java's XML parser
- **XmlBuilder**: Converts XML DOM structures into Logiqua scripts

## Usage

### Maven Coordinates

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>xml</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Basic Usage

#### Simple Expression

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="==">
        <constant integer="3"/>
        <constant integer="5"/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = false
```

#### With Context Data

```java
Map<String, Object> data = Map.of("x", 10, "y", 20);
XmlLogiqua xml = new XmlLogiqua().with(data);
String script = """
    <op symbol="+">
        <var id="x"/>
        <var id="y"/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = 30
```

#### Using Custom Engine

```java
Engine engine = Engine.withData(Map.of("x", 10));
XmlLogiqua xml = new XmlLogiqua().with(engine);
String script = """
    <var id="x"/>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = 10
```

## Syntax

### XML Structure

XML expressions use a hierarchical structure with special tags:

- **`<op symbol="...">`**: Operation with symbol name
- **`<var id="...">`**: Variable access
- **`<constant integer="...">`**: Integer constant
- **`<constant float="...">`**: Floating-point constant
- **`<constant string="...">`**: String constant
- **`<list>`**: Array/list of values
- **`<true>`**: Boolean true value
- **`<false>`**: Boolean false value
- **`<null>`**: Null value

### Basic Syntax

Operations are represented as XML elements with a `symbol` attribute:

```xml
<op symbol="operationName">
    <argument1/>
    <argument2/>
</op>
```

### Examples

#### Arithmetic Operations

```xml
<!-- Addition -->
<op symbol="+">
    <constant integer="1"/>
    <constant integer="2"/>
    <constant integer="3"/>
</op>

<!-- Subtraction -->
<op symbol="-">
    <constant integer="10"/>
    <constant integer="3"/>
</op>

<!-- Multiplication -->
<op symbol="*">
    <constant integer="2"/>
    <constant integer="3"/>
    <constant integer="4"/>
</op>

<!-- Division -->
<op symbol="/">
    <constant integer="10"/>
    <constant integer="2"/>
</op>

<!-- Modulo -->
<op symbol="%">
    <constant integer="10"/>
    <constant integer="3"/>
</op>
```

#### Comparisons

```xml
<!-- Equality -->
<op symbol="==">
    <constant integer="3"/>
    <constant integer="5"/>
</op>

<!-- Inequality -->
<op symbol="!=">
    <constant integer="3"/>
    <constant integer="5"/>
</op>

<!-- Less than -->
<op symbol="&lt;">
    <constant integer="3"/>
    <constant integer="5"/>
</op>

<!-- Less than or equal -->
<op symbol="&lt;=">
    <constant integer="3"/>
    <constant integer="5"/>
</op>

<!-- Greater than -->
<op symbol="&gt;">
    <constant integer="5"/>
    <constant integer="3"/>
</op>

<!-- Greater than or equal -->
<op symbol="&gt;=">
    <constant integer="5"/>
    <constant integer="3"/>
</op>
```

> **Note:** In XML, special characters like `<` and `>` must be escaped as `&lt;` and `&gt;` respectively, or the entire expression can be wrapped in a CDATA section.

#### Logical Operations

```xml
<!-- Logical AND -->
<op symbol="and">
    <true/>
    <true/>
    <false/>
</op>

<!-- Logical OR -->
<op symbol="or">
    <false/>
    <true/>
</op>

<!-- Logical NOT -->
<op symbol="!">
    <false/>
</op>
```

#### Conditional

```xml
<op symbol="if">
    <true/>
    <constant string="yes"/>
    <constant string="no"/>
</op>
```

#### Variable Access

```xml
<!-- Simple variable -->
<var id="x"/>

<!-- Nested access (using dot notation in id) -->
<var id="user.name"/>

<!-- Array access (using bracket notation in id) -->
<var id="items[0]"/>
```

#### Constants

```xml
<!-- Integer constant -->
<constant integer="42"/>

<!-- Floating-point constant -->
<constant float="3.14"/>

<!-- String constant -->
<constant string="Hello, World!"/>

<!-- Boolean constants -->
<true/>
<false/>

<!-- Null value -->
<null/>
```

#### Lists and Arrays

```xml
<list>
    <constant integer="1"/>
    <constant integer="2"/>
    <constant integer="3"/>
    <constant integer="4"/>
    <constant integer="5"/>
</list>
```

#### String Operations

```xml
<!-- Concatenation -->
<op symbol="cat">
    <constant string="Hello"/>
    <constant string=" "/>
    <constant string="World"/>
</op>

<!-- Substring -->
<op symbol="substr">
    <constant string="Hello World"/>
    <constant integer="0"/>
    <constant integer="5"/>
</op>
```

#### Array Operations

```xml
<!-- Filter -->
<op symbol="filter">
    <list>
        <constant integer="1"/>
        <constant integer="2"/>
        <constant integer="3"/>
        <constant integer="4"/>
        <constant integer="5"/>
        <constant integer="6"/>
        <constant integer="7"/>
        <constant integer="8"/>
        <constant integer="9"/>
    </list>
    <op symbol="==">
        <op symbol="%">
            <var id="current"/>
            <constant integer="2"/>
        </op>
        <constant integer="0"/>
    </op>
</op>

<!-- Map -->
<op symbol="map">
    <list>
        <constant integer="23"/>
        <constant integer="24"/>
    </list>
    <op symbol="*">
        <var id="current"/>
        <constant integer="2"/>
    </op>
</op>

<!-- Reduce -->
<op symbol="reduce">
    <list>
        <constant integer="1"/>
        <constant integer="2"/>
        <constant integer="3"/>
    </list>
    <op symbol="+">
        <var id="accumulator"/>
        <var id="current"/>
    </op>
    <constant integer="0"/>
</op>
```

### Nested Expressions

Expressions can be nested to any depth:

```xml
<op symbol="if">
    <op symbol="and">
        <op symbol="&lt;">
            <var id="x"/>
            <constant integer="10"/>
        </op>
        <op symbol="&gt;">
            <var id="y"/>
            <constant integer="5"/>
        </op>
    </op>
    <op symbol="+">
        <var id="x"/>
        <var id="y"/>
    </op>
    <op symbol="-">
        <var id="x"/>
        <var id="y"/>
    </op>
</op>
```

## Usage Examples

### Arithmetic

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="+">
        <constant integer="1"/>
        <constant integer="2"/>
        <constant integer="3"/>
        <constant integer="4"/>
        <constant integer="5"/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = 15
```

### Comparisons

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="&lt;">
        <constant integer="1"/>
        <constant integer="3"/>
        <constant integer="5"/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = true
```

### Logical Operations

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="and">
        <true/>
        <true/>
        <true/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = true
```

### Conditional Logic

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="if">
        <op symbol="&lt;">
            <constant integer="3"/>
            <constant integer="5"/>
        </op>
        <constant string="yes"/>
        <constant string="no"/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = "yes"
```

### Variable Access

```java
Map<String, Object> data = Map.of("name", "Logiqua", "version", 1.0);
XmlLogiqua xml = new XmlLogiqua().with(data);
String script = """
    <op symbol="cat">
        <var id="name"/>
        <constant string=" v"/>
        <var id="version"/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = "Logiqua v1.0"
```

### Array Filtering

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="filter">
        <list>
            <constant integer="1"/>
            <constant integer="2"/>
            <constant integer="3"/>
            <constant integer="4"/>
            <constant integer="5"/>
            <constant integer="6"/>
            <constant integer="7"/>
            <constant integer="8"/>
            <constant integer="9"/>
        </list>
        <op symbol="==">
            <op symbol="%">
                <var id="current"/>
                <constant integer="2"/>
            </op>
            <constant integer="0"/>
        </op>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = [2, 4, 6, 8]
```

### Array Mapping

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="map">
        <list>
            <constant integer="23"/>
            <constant integer="24"/>
        </list>
        <op symbol="*">
            <var id="current"/>
            <constant integer="2"/>
        </op>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = [46, 48]
```

### Array Reduction

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="reduce">
        <list>
            <constant integer="1"/>
            <constant integer="2"/>
            <constant integer="3"/>
        </list>
        <op symbol="+">
            <var id="accumulator"/>
            <var id="current"/>
        </op>
        <constant integer="0"/>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = 6
```

### Complex Nested Expressions

```java
XmlLogiqua xml = new XmlLogiqua();
String script = """
    <op symbol="if">
        <op symbol="and">
            <op symbol="&lt;">
                <constant integer="1"/>
                <constant integer="5"/>
            </op>
            <op symbol="&gt;">
                <constant integer="3"/>
                <constant integer="1"/>
            </op>
        </op>
        <op symbol="+">
            <constant integer="10"/>
            <constant integer="20"/>
        </op>
        <op symbol="-">
            <constant integer="10"/>
            <constant integer="20"/>
        </op>
    </op>
    """;
Script compiled = xml.compile(script);
Object result = compiled.evaluate();
// result = 30
```

## Special Features

### Local Context Variables

In array operations (`filter`, `map`, `reduce`, `all`, `some`, `none`), the current element is available as:

- `"current"`: The current element being processed
- `"accumulator"`: (For `reduce` only) The accumulator value

```xml
<op symbol="filter">
    <list>
        <constant integer="1"/>
        <constant integer="2"/>
        <constant integer="3"/>
        <constant integer="4"/>
    </list>
    <op symbol="==">
        <op symbol="%">
            <var id="current"/>  <!-- Current element -->
            <constant integer="2"/>
        </op>
        <constant integer="0"/>
    </op>
</op>
```

### Type Specification

Constants must explicitly specify their type:

- **`<constant integer="...">`**: For integer values (parsed as Long)
- **`<constant float="...">`**: For floating-point values (parsed as Double)
- **`<constant string="...">`**: For string values

Only one type attribute can be specified per constant element.

### XML Escaping

In XML, special characters must be escaped:

- `<` must be written as `&lt;`
- `>` must be written as `&gt;`
- `&` must be written as `&amp;`
- `"` must be written as `&quot;`
- `'` must be written as `&apos;`

Alternatively, you can use CDATA sections for complex expressions:

```xml
<op symbol="&lt;">
    <![CDATA[
        <constant integer="3"/>
        <constant integer="5"/>
    ]]>
</op>
```

### Well-Formed XML

The XML must be well-formed:

- All tags must be properly closed
- Attributes must be quoted
- Special characters must be escaped
- The document must have a single root element

## Error Handling

The parser will throw `IllegalArgumentException` for:

- Invalid XML syntax
- Unknown operations
- Type mismatches
- Missing required attributes
- Invalid constant specifications

```java
try {
    Script script = xml.compile(invalidXml);
    script.evaluate();
} catch (IllegalArgumentException e) {
    // Handle parsing or evaluation errors
}
```

## Integration with Logiqua

The XML module is fully integrated with the Logiqua ecosystem:

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
- JUnit Jupiter (for testing)

## License

See the main project LICENSE file for license information.

## See Also

- [API Documentation](target/reports/apidocs/index.html) - Full API reference
- [API Module README](../api/README.md) - Core API documentation
- [Engine Module README](../engine/README.md) - Execution engine documentation
- [Commands Module README](../commands/README.md) - Standard operations documentation
- Main project README for overall project information


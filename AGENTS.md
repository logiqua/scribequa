# AGENTS.md

Guidelines for AI agents working with Logiqua codebase.

## Architecture

### Modules
- `api`: Core interfaces (`Logiqua`, `Script`, `Executor`, `Context`, `Operation`)
- `engine`: Execution engine implementing `Executor` and `Builder`
- `commands`: Standard operations library (ServiceLoader auto-discovery)
- Format modules: `json`, `yaml`, `xml`, `lsp`, `exp` (implement `Logiqua`)
- `jsonlogic`: JsonLogic compatibility
- `lex`: Lexical analysis framework
- `fx`: GUI playground

### Core Interfaces

**Script**
- `Object evaluate()` - Evaluate script
- `Object evaluateUsing(Executor executor)` - Evaluate with executor
- `String jsonify()` - Convert to JSON (canonical format)
- `Set<Class<?>> returns()` - Return type information

**Logiqua**
- `Script compile(String script)` - Parse and compile

**Operation**
- `Operation.Function`: Arguments evaluated before call
- `Operation.Command`/`Operation.Macro`: Receives unevaluated arguments

## Conventions

### Variables
- Use `final var` for locals
- Prefer descriptive names

### Tests
- One `@Test` method per scenario
- Descriptive method names: `testBytePlusShort()`, `testIntegerDividedByFloat()`
- Cannot reuse `ExpLogiqua` instances with context - create new instance per compile
- Use direct variable names: `aByte + aShort` not `var("aByte") + var("aShort")`
- Nested paths: `var("path.to.value")`
- Array operations: `current` or `var("")` for current element, `accumulator` for reduce

### Expression Module Syntax
- Direct access: `x + y` (when in context)
- Nested: `var("user.name")`, `var("items[0]")`
- Array ops: `current` (element), `accumulator` (reduce)
- **CRITICAL**: Create new `ExpLogiqua().with(context).compile(script)` for each compile

### Type Promotion (Operator class)
- Order: Byte < Short < Integer < Long < Float < Double < BigInteger < BigDecimal
- Integer + Float → Float
- BigInteger/BigDecimal + Float → BigDecimal
- Methods: `toByte()`, `toShort()`, `toInteger()`, `toLong()`, `toFloat()`, `toDouble()`, `toBigInteger()`, `toBigDecimal()`
- Use `NumericType.promote()` for common type

### JSON Format (jsonify())
- Output: `{"operation": [args...]}`
- Canonical format for storage
- All scripts convertible regardless of original format

## Patterns

### Script with Context
```java
// Correct
Script script = new ExpLogiqua().with(context).compile(expression);

// Wrong - cannot reuse
ExpLogiqua exp = new ExpLogiqua().with(context);
Script s1 = exp.compile("...");  // OK
Script s2 = exp.compile("...");  // ERROR
```

### Testing Numeric Conversions
- Test all type combinations
- Verify result value and type
- Use `final var result`

### Testing Complex Expressions
- Deeply nested structures (maps, lists, primitives, wrappers)
- Multi-level expressions with parentheses
- Verify evaluation and `jsonify()` output
- Check JSON structure: balanced brackets, expected operations

## Dependencies

### Test Scope
- `exp` tests can use `commands` module
- Format modules independent
- `jsonlogic` depends on `json`

### Module Access
- Check `module-info.java` for exports
- Use public APIs from `api` module

## Critical Rules

1. **ExpLogiqua**: Cannot reuse instances with context - create new per compile
2. **Variable access**: Direct names when possible, `var()` for nested paths
3. **Array operations**: `current` or `var("")` for element, `accumulator` for reduce
4. **Type promotion**: Automatic via `Operator` class
5. **JSON conversion**: All scripts support `jsonify()`

## Files

- `api/src/main/java/com/javax0/logiqua/Script.java`
- `api/src/main/java/com/javax0/logiqua/Logiqua.java`
- `engine/src/main/java/com/javax0/logiqua/engine/Engine.java`
- `commands/src/main/java/com/javax0/logiqua/commands/Operator.java`
- `exp/src/main/java/com/javax0/logiqua/exp/ExpLogiqua.java`

## Commands

```bash
mvn clean install
mvn test
cd <module> && mvn test
cd fx && mvn javafx:run
```

## License

Dual-licensed: MIT License and Apache License 2.0. File: `LICENSES`

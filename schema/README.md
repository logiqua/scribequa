# Schema Module

The Schema module answers one question about a variable path: **can the data ever have a value there?**

## The problem

A `Context` has two answers for a variable read, a value or nothing, and "nothing" covers two very
different situations:

```json
{"var": ["user.age", 0]}
{"var": ["user.aeg", 0]}
```

The first reads an optional field that this particular record happens to lack, and `0` is exactly the
right answer. The second is a typo, and `0` is the worst possible answer, because the rule now returns
a plausible number forever and nobody finds out. Without a description of the data there is no way to
tell the two apart: both names are simply absent.

A schema is that description. Given one, the second read throws a
`com.javax0.logiqua.SchemaViolationException`, and the first keeps returning `0`.

## Usage

```xml
<dependency>
    <groupId>com.javax0.logiqua</groupId>
    <artifactId>schema</artifactId>
    <version>2.0.2</version>
</dependency>
```

```java
final var schema = JsonSchema.of(schemaJson);
final var engine = Engine.withData(data, schema);
final var script = new JsonLogiqua().with(engine).compile(rule);
script.evaluate();
```

Every front end takes an `Engine`, so the same two lines work for `JsonLogiqua`, `YamlLogiqua`,
`XmlLogiqua`, `LspLogiqua` and `ExpLogiqua`. JsonLogic has a direct overload:

```java
new JsonLogic().apply(rule, data, schema);
```

## JsonSchema

`JsonSchema` reads a JSON Schema document. It does not validate data with it; it only walks the
structural keywords to decide whether a path is reachable. `type`, `properties`, `patternProperties`,
`additionalProperties`, `prefixItems`, `items`, `additionalItems`, `maxItems`, the boolean schemas
`true` and `false`, the combinators `allOf`, `anyOf` and `oneOf`, and `$ref` into the same document
are understood. The value constraints, `minimum`, `pattern`, `format` and the rest, are ignored,
because a value that is out of range is a data problem and this module is about script problems.

```java
final var schema = JsonSchema.of("""
        {
          "type": "object",
          "additionalProperties": false,
          "properties": {
            "user": {
              "type": "object",
              "additionalProperties": false,
              "properties": {
                "name": { "type": "string" },
                "age":  { "type": "integer" }
              }
            }
          }
        }
        """);

schema.verdict("user.name");   // DESCRIBED
schema.verdict("user.age");    // DESCRIBED, even when this record has no age
schema.verdict("user.aeg");    // IMPOSSIBLE
schema.verdict("user.age.y");  // IMPOSSIBLE, an integer has no members
```

### `closed()`

A JSON Schema that lists `properties` but does not say `"additionalProperties": false` permits every
other property. That is what the specification means, and it is why the schema above needs
`additionalProperties` on every object to catch anything. When the schema exists to catch mistyped
variables rather than to validate incoming documents, that ceremony is noise:

```java
final var schema = JsonSchema.of(document).closed();
```

`closed()` reads an object that lists its properties, and an array that lists its `prefixItems`, as the
complete inventory. An explicit `additionalProperties` or `items` in the document still wins, and an
object that describes no properties at all stays open.

## PathSchema

When no JSON Schema document exists, writing one only to catch typos is a lot of ceremony for a small
job. `PathSchema` takes the list of paths the scripts may read:

```java
final var schema = PathSchema.of(
        "user.name",
        "user.address.*",   // any single segment
        "orders[*].total",  // * matches an index too
        "extras.**");       // the whole subtree

schema.verdict("user.name");        // DESCRIBED
schema.verdict("user");             // DESCRIBED, an intermediate node of a described path
schema.verdict("orders[2].total");  // DESCRIBED
schema.verdict("extras.a.b.c");     // DESCRIBED
schema.verdict("user.nmae");        // IMPOSSIBLE
```

## Writing your own

`Schema` is a functional interface in the `api` module with a single method. Anything that knows the
shape of the data can implement it, a Java record, a database catalog, an OpenAPI document:

```java
final Schema schema = path -> known.contains(path)
        ? Schema.Judgement.described()
        : Schema.Judgement.impossible("'" + path + "' is not a field of the request");
```

The three verdicts are `DESCRIBED`, `IMPOSSIBLE` and `UNCONSTRAINED`. The last one is for the parts of
a structure a schema deliberately leaves open, and it is what `Schema.OPEN` always answers. A
`SchemaCheckedContext` lets `UNCONSTRAINED` through by default and rejects it in
`Strictness.STRICT`, so a schema never has to pretend to know more than it does.

The path syntax is the one `Context.get(String)` uses, and `Context.segments(String)` splits it the
same way the contexts do.

## What is not checked

The loop commands, `map`, `filter`, `reduce`, `all`, `some` and `none`, evaluate their body in a local
scope where the names resolve against the current element first:

```json
{"map": [{"var": "orders"}, {"var": "total"}]}
```

Here `total` is a field of an order, not of the data root, and the loop commands do not tell the
context which path the elements came from. Rather than guess and reject an element's legitimate
optional field, `SchemaCheckedContext.sprout` hands out an unchecked local context, so loop bodies are
not checked. `Schema.at(String)` narrows a schema to a sub-structure for a caller that does know the
path:

```java
final var orderSchema = schema.at("orders[0]");
orderSchema.verdict("total");  // DESCRIBED
orderSchema.verdict("totl");   // IMPOSSIBLE
```

Checking happens when a variable is read, not when a script is compiled. A rule with a typo in a
branch that never executes stays quiet until that branch runs.

package com.javax0.logiqua.schema;

import com.javax0.lex.LexicalAnalyzer;
import com.javax0.lex.StringInput;
import com.javax0.lex.TokenIterator;
import com.javax0.lex.tokens.NewLine;
import com.javax0.lex.tokens.Space;
import com.javax0.logiqua.Context;
import com.javax0.logiqua.Schema;
import com.javax0.logiqua.json.JsonReader;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * A {@link Schema} backed by a JSON Schema document.
 * <p>
 * The document is not used to validate data. It is used to answer a single question about a variable
 * path: can any document that satisfies this schema ever carry a value there? The keywords that
 * describe structure are the ones that matter, and the keywords that constrain values, such as
 * {@code minimum}, {@code pattern} or {@code format}, are ignored.
 * <p>
 * The supported keywords are {@code type}, {@code properties}, {@code patternProperties},
 * {@code additionalProperties}, {@code prefixItems}, {@code items}, {@code additionalItems},
 * {@code maxItems}, the boolean schemas {@code true} and {@code false}, the combinators
 * {@code allOf}, {@code anyOf} and {@code oneOf}, and {@code $ref} pointing inside the document,
 * which covers {@code $defs} and {@code definitions}.
 * <p>
 * Where the schema is silent, this class answers {@link Verdict#UNCONSTRAINED} rather than guessing.
 * Two paths are worth spelling out because they surprise people:
 *
 * <ul>
 *     <li>An object that lists {@code properties} but does not say {@code "additionalProperties": false}
 *         permits any other property, because that is what JSON Schema means. Such a schema catches no
 *         typos.</li>
 *     <li>A combinator branch that allows a path makes the whole path allowed, even if the other
 *         branches exclude it.</li>
 * </ul>
 * <p>
 * {@link #closed()} switches the first of these off. It reads an object that lists its properties, and
 * an array that lists its {@code prefixItems}, as the complete inventory, so a name that is not in the
 * document is impossible. That is usually what you want when the schema exists to catch mistyped
 * variables rather than to validate incoming documents.
 *
 * <pre>{@code
 * final var schema = JsonSchema.of(schemaJson).closed();
 * final var engine = Engine.withData(data, schema);
 * new JsonLogiqua().with(engine).compile(script).evaluate();  // throws on var("user.nmae")
 * }</pre>
 */
public class JsonSchema implements Schema {

    private static final List<String> COMBINATORS = List.of("allOf", "anyOf", "oneOf");
    private static final Set<String> STRUCTURAL = Set.of(
            "type", "properties", "patternProperties", "additionalProperties",
            "items", "prefixItems", "additionalItems");
    private static final int MAX_DEPTH = 100;

    private final Object root;
    private final Object document;
    private final boolean closed;
    private final Map<String, Pattern> patterns = new ConcurrentHashMap<>();

    private JsonSchema(Object root, Object document, boolean closed) {
        this.root = root;
        this.document = document;
        this.closed = closed;
    }

    /**
     * Build a schema from a JSON Schema document.
     *
     * @param json the document as JSON text
     * @return the schema
     */
    public static JsonSchema of(String json) {
        final var analyzer = new LexicalAnalyzer();
        analyzer.skip(Space.class);
        analyzer.skip(NewLine.class);
        final var tokens = TokenIterator.over(analyzer.analyse(StringInput.of(json)));
        return of(JsonReader.of(tokens).read());
    }

    /**
     * Build a schema from an already parsed JSON Schema document. This is the entry point for a
     * document that came from somewhere else, a YAML file or a hand-built map, for example.
     *
     * @param document the parsed document, a {@code Map}, or a {@code Boolean} for the trivial schemas
     * @return the schema
     */
    public static JsonSchema of(Object document) {
        return new JsonSchema(document, document, false);
    }

    /**
     * Read the document as a complete inventory of the data.
     * <p>
     * An object that lists {@code properties} is taken to have no other property, and an array that
     * lists {@code prefixItems} is taken to have no further element, unless the document says
     * otherwise with an explicit {@code additionalProperties} or {@code items}. This is the setting
     * that turns a schema into a typo detector.
     *
     * @return a schema over the same document that treats the described structure as complete
     */
    public JsonSchema closed() {
        return closed ? this : new JsonSchema(root, document, true);
    }

    /**
     * @return whether this schema reads the document as a complete inventory, see {@link #closed()}
     */
    public boolean isClosed() {
        return closed;
    }

    @Override
    public Judgement judge(String path) {
        if (path == null || path.isEmpty()) {
            return Judgement.described();
        }
        final var segments = Context.segments(path);
        var nodes = expand(root, 0);
        final var walked = new StringBuilder();
        for (final var segment : segments) {
            final var next = new ArrayList<>();
            var open = false;
            for (final var node : nodes) {
                for (final var child : children(node, segment)) {
                    if (child == Boolean.TRUE) {
                        open = true;
                    } else {
                        next.addAll(expand(child, 0));
                    }
                }
            }
            if (next.isEmpty() && !open) {
                return Judgement.impossible("there is no '" + segment + "' in "
                        + (walked.isEmpty() ? "the root of the schema" : "'" + walked + "'"));
            }
            if (open) {
                next.add(Boolean.TRUE);
            }
            nodes = next;
            if (!walked.isEmpty()) {
                walked.append('.');
            }
            walked.append(segment);
        }
        return nodes.stream().anyMatch(node -> !(node instanceof Boolean))
                ? Judgement.described()
                : Judgement.unconstrained();
    }

    /**
     * Flatten a schema node into the alternatives that a path may follow. A {@code $ref} is resolved,
     * and a combinator contributes each of its branches. The result is permissive on purpose: a path
     * is only impossible when every alternative excludes it.
     *
     * @param node  the schema node
     * @param depth the recursion depth, to survive a document that refers to itself
     * @return the alternatives, never empty
     */
    private List<Object> expand(Object node, int depth) {
        final var resolved = deref(node, 0);
        if (resolved instanceof Boolean) {
            return List.of(resolved);
        }
        if (depth > MAX_DEPTH || !(resolved instanceof Map<?, ?> map)) {
            return List.of(Boolean.TRUE);
        }
        final var alternatives = new ArrayList<>();
        var combined = false;
        for (final var combinator : COMBINATORS) {
            if (map.get(combinator) instanceof List<?> branches) {
                combined = true;
                for (final var branch : branches) {
                    alternatives.addAll(expand(branch, depth + 1));
                }
            }
        }
        if (!combined || STRUCTURAL.stream().anyMatch(map::containsKey)) {
            alternatives.add(map);
        }
        return alternatives.isEmpty() ? List.of(Boolean.TRUE) : alternatives;
    }

    /**
     * The schemas a segment may have under a node.
     *
     * @param node    the schema node
     * @param segment one path segment, a property name or an array index
     * @return the possible child schemas. An empty list means the node excludes the segment, and
     * {@link Boolean#TRUE} in the list means anything below is allowed.
     */
    private List<Object> children(Object node, String segment) {
        if (node instanceof Boolean allowed) {
            return allowed ? List.of(Boolean.TRUE) : List.of();
        }
        if (!(node instanceof Map<?, ?> map)) {
            return List.of(Boolean.TRUE);
        }
        final var types = types(map);
        final var children = new ArrayList<>();
        if (types.isEmpty() || types.contains("object")) {
            add(children, objectChild(map, segment));
        }
        final var index = index(segment);
        if (index >= 0 && (types.isEmpty() || types.contains("array"))) {
            add(children, arrayChild(map, index));
        }
        return children;
    }

    private static void add(List<Object> children, Object child) {
        if (child != null && child != Boolean.FALSE) {
            children.add(child);
        }
    }

    /**
     * @return the schema of the property, {@code null} when the object cannot have it, or
     * {@link Boolean#TRUE} when the object may have it but the schema does not describe it
     */
    private Object objectChild(Map<?, ?> map, String key) {
        if (map.get("properties") instanceof Map<?, ?> properties && properties.containsKey(key)) {
            return properties.get(key);
        }
        if (map.get("patternProperties") instanceof Map<?, ?> patternProperties) {
            for (final var entry : patternProperties.entrySet()) {
                if (entry.getKey() instanceof String regex && matches(regex, key)) {
                    return entry.getValue();
                }
            }
        }
        final var additional = map.get("additionalProperties");
        if (additional != null) {
            return additional;
        }
        final var describesProperties = map.containsKey("properties") || map.containsKey("patternProperties");
        return closed && describesProperties ? null : Boolean.TRUE;
    }

    /**
     * @return the schema of the element, {@code null} when the array cannot reach the index, or
     * {@link Boolean#TRUE} when the element is allowed but not described
     */
    private Object arrayChild(Map<?, ?> map, int index) {
        if (map.get("maxItems") instanceof Number max && index >= max.intValue()) {
            return null;
        }
        // "items" as a list is the draft-07 spelling of "prefixItems"
        final var tuple = map.get("prefixItems") instanceof List<?> prefixItems ? prefixItems
                : map.get("items") instanceof List<?> itemList ? itemList : null;
        if (tuple != null) {
            if (index < tuple.size()) {
                return tuple.get(index);
            }
            final var tail = map.containsKey("prefixItems") ? map.get("items") : map.get("additionalItems");
            return tail != null ? tail : closed ? null : Boolean.TRUE;
        }
        final var items = map.get("items");
        return items != null ? items : Boolean.TRUE;
    }

    /**
     * Resolve a {@code $ref} chain. An unresolvable reference, an external one for instance, yields
     * the permissive schema rather than an error, so that a partially known document still works.
     *
     * @param node the node that may be a reference
     * @param hops how many references were already followed, to survive a reference cycle
     * @return the referenced node
     */
    private Object deref(Object node, int hops) {
        if (hops > MAX_DEPTH) {
            return Boolean.TRUE;
        }
        if (node instanceof Map<?, ?> map && map.get("$ref") instanceof String reference) {
            final var target = pointer(reference);
            return target == null ? Boolean.TRUE : deref(target, hops + 1);
        }
        return node;
    }

    /**
     * @param reference a JSON pointer reference such as {@code #/$defs/address}
     * @return the referenced node, or {@code null} when it does not point inside this document
     */
    private Object pointer(String reference) {
        if (!reference.startsWith("#")) {
            return null;
        }
        final var pointer = reference.substring(1);
        if (pointer.isEmpty() || pointer.equals("/")) {
            return document;
        }
        if (!pointer.startsWith("/")) {
            return null;
        }
        var node = document;
        for (final var escaped : pointer.substring(1).split("/", -1)) {
            final var token = escaped.replace("~1", "/").replace("~0", "~");
            switch (node) {
                case Map<?, ?> map -> {
                    if (!map.containsKey(token)) {
                        return null;
                    }
                    node = map.get(token);
                }
                case List<?> list -> {
                    final var index = index(token);
                    if (index < 0 || index >= list.size()) {
                        return null;
                    }
                    node = list.get(index);
                }
                case null, default -> {
                    return null;
                }
            }
        }
        return node;
    }

    private static Set<String> types(Map<?, ?> map) {
        return switch (map.get("type")) {
            case String type -> Set.of(type);
            case List<?> list -> list.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toUnmodifiableSet());
            case null, default -> Set.of();
        };
    }

    private boolean matches(String regex, String key) {
        final var pattern = patterns.computeIfAbsent(regex, r -> {
            try {
                return Pattern.compile(r);
            } catch (PatternSyntaxException e) {
                return null;
            }
        });
        return pattern != null && pattern.matcher(key).find();
    }

    /**
     * @param segment a path segment
     * @return the segment as an array index, or {@code -1} when it is not one
     */
    private static int index(String segment) {
        if (segment.isEmpty()) {
            return -1;
        }
        long value = 0;
        for (final var ch : segment.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return -1;
            }
            value = value * 10 + Character.getNumericValue(ch);
            if (value > Integer.MAX_VALUE) {
                return -1;
            }
        }
        return (int) value;
    }
}

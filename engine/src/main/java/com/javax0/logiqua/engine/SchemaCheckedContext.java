package com.javax0.logiqua.engine;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Schema;
import com.javax0.logiqua.SchemaViolationException;

import java.util.Map;
import java.util.Optional;

/**
 * A {@link Context} that consults a {@link Schema} before it delegates a variable read to another
 * context.
 * <p>
 * Every variable read of every script funnels through {@link Context#get(String)}, so this one class
 * is enough to turn "the variable is not there" into "the variable can never be there" for the whole
 * language. A plain context has only two answers, a value or {@code null}, and {@code null} conflates
 * an optional field that happens to be absent with a misspelled field name. This decorator adds the
 * third answer by throwing a {@link SchemaViolationException}.
 * <p>
 * The exception is thrown before the delegate is consulted, so it also fires for the two-argument
 * {@code var} that carries a default value. That is the point. A default value is the right answer for
 * a field that may be absent, and the wrong answer for a field that does not exist.
 *
 * <h2>Local scopes</h2>
 * The loop commands, {@code map}, {@code filter}, {@code reduce} and their relatives, evaluate their
 * body in a context {@linkplain Context#sprout(Map) sprouted} from this one, where the names resolve
 * against the current element first. The element's own shape is a sub-schema that the loop commands do
 * not know the path of, so this class cannot judge a name in a loop body without producing false
 * alarms for the element's optional fields. {@link #sprout(Map)} therefore delegates to the unchecked
 * context and loop bodies are not checked. {@link Schema#at(String)} is the hook for narrowing a
 * schema to a sub-structure when a caller does know the path.
 */
public class SchemaCheckedContext implements Context {

    /**
     * How to treat a path that the schema neither describes nor excludes.
     */
    public enum Strictness {
        /**
         * Only a path that the schema proves impossible is rejected. This is the default and it is the
         * safe setting for a schema that leaves parts of the structure open on purpose.
         */
        LENIENT,
        /**
         * A path that the schema does not describe is rejected too. Use this when the schema is meant
         * to be the complete inventory of the data and anything outside it is a typo.
         */
        STRICT
    }

    private final Context delegate;
    private final Schema schema;
    private final Strictness strictness;

    /**
     * Wrap a context with a lenient schema check.
     *
     * @param delegate the context that actually holds the data
     * @param schema   the schema that describes the data
     */
    public SchemaCheckedContext(Context delegate, Schema schema) {
        this(delegate, schema, Strictness.LENIENT);
    }

    /**
     * @param delegate   the context that actually holds the data
     * @param schema     the schema that describes the data
     * @param strictness what to do with a path the schema says nothing about
     */
    public SchemaCheckedContext(Context delegate, Schema schema, Strictness strictness) {
        this.delegate = delegate;
        this.schema = schema == null ? Schema.OPEN : schema;
        this.strictness = strictness;
    }

    /**
     * Convenience factory that wraps a plain {@link MapContext} over the given data.
     *
     * @param data   the data of the context
     * @param schema the schema that describes the data
     * @return the new checked context
     */
    public static SchemaCheckedContext of(Map<String, Object> data, Schema schema) {
        return new SchemaCheckedContext(new MapContext(data), schema);
    }

    /**
     * Ask the schema about the key and, if it survives, delegate the read.
     *
     * The empty key, which reads the whole data, is never rejected.
     *
     * @param key the key to look up
     * @return whatever the delegate returns, {@code null} when the value is simply absent
     * @throws SchemaViolationException if the schema rules the key out
     */
    @Override
    public Value get(String key) {
        final var path = key == null ? "" : key;
        if (path.isEmpty()) {
            // the empty path is the data itself, which is what the schema describes, so it is always
            // readable. Guaranteeing it here spares every Schema implementation the special case.
            return delegate.get(path);
        }
        final var judgement = schema.judge(path);
        switch (judgement.verdict()) {
            case IMPOSSIBLE -> throw new SchemaViolationException(path, judgement.reason());
            case UNCONSTRAINED -> {
                if (strictness == Strictness.STRICT) {
                    throw new SchemaViolationException(path, "the schema does not describe it and the context is strict");
                }
            }
            case DESCRIBED -> {
            }
        }
        return delegate.get(path);
    }

    @Override
    public Proxy accessor(Object target) {
        return delegate.accessor(target);
    }

    @Override
    public <From, To> Optional<Caster<From, To>> caster(Class<From> from, Class<To> to) {
        return delegate.caster(from, to);
    }

    /**
     * Sprout an <em>unchecked</em> local context. See the class documentation for why the loop bodies
     * are not checked.
     *
     * @param data the data of the local scope
     * @return the local context, delegating to the unchecked context this one wraps
     */
    @Override
    public Context sprout(Map<String, Object> data) {
        return delegate.sprout(data);
    }

    @Override
    public Schema schema() {
        return schema;
    }

    /**
     * @return the context that holds the data, without the schema check
     */
    public Context delegate() {
        return delegate;
    }
}

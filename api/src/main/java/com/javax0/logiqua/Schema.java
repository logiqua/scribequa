package com.javax0.logiqua;

/**
 * A description of the shape of the data that an execution {@link Context} may hold.
 * <p>
 * A {@code Schema} does not tell whether a variable <em>is</em> defined; that is what
 * {@link Context#get(String)} is for. It tells whether a variable <em>could ever</em> be defined.
 * These are two different failures:
 *
 * <ul>
 *     <li>{@code var("user.name")} where {@code name} is an optional field that happens to be absent
 *         is a normal, expected run-time situation. The script may supply a default value for it.</li>
 *     <li>{@code var("user.nmae")} where the schema says that a {@code user} has no {@code nmae} field
 *         at all is a programming error. No data set will ever make this expression work, and silently
 *         returning the default value would hide the typo forever.</li>
 * </ul>
 * <p>
 * The second case is what a schema is for. A {@link Context} that knows its schema, like the
 * {@code SchemaCheckedContext} of the engine module, throws a {@link SchemaViolationException} for it
 * instead of reporting "not found".
 * <p>
 * The interface is intentionally tiny and independent of JSON Schema. Any structure description can
 * implement it: a JSON Schema document, a list of allowed paths, a Java record, a database catalog.
 * <p>
 * The path syntax is the one that {@link Context#get(String)} uses, split into segments by
 * {@link Context#segments(String)}.
 */
@FunctionalInterface
public interface Schema {

    /**
     * What a schema thinks about a variable path.
     */
    enum Verdict {
        /**
         * The schema describes this path. The value may still be absent at run time.
         */
        DESCRIBED,
        /**
         * The schema says nothing about this path. It neither describes nor excludes it. This is the
         * verdict for the open parts of a structure, for example, an object that permits additional
         * properties.
         */
        UNCONSTRAINED,
        /**
         * The schema proves that this path can never carry a value. Reading it is an error.
         */
        IMPOSSIBLE
    }

    /**
     * The verdict together with the human-readable reason that explains it.
     *
     * @param verdict the verdict
     * @param reason  why the verdict was reached, or {@code null} when there is nothing to explain.
     *                Implementations should fill this in for {@link Verdict#IMPOSSIBLE} because it is
     *                the text the user sees in the exception message.
     */
    record Judgement(Verdict verdict, String reason) {
        private static final Judgement DESCRIBED = new Judgement(Verdict.DESCRIBED, null);
        private static final Judgement UNCONSTRAINED = new Judgement(Verdict.UNCONSTRAINED, null);

        public static Judgement described() {
            return DESCRIBED;
        }

        public static Judgement unconstrained() {
            return UNCONSTRAINED;
        }

        public static Judgement impossible(String reason) {
            return new Judgement(Verdict.IMPOSSIBLE, reason);
        }
    }

    /**
     * Decide whether the given path can ever carry a value.
     *
     * @param path the variable path, in the syntax of {@link Context#get(String)}
     * @return the judgement, never {@code null}
     */
    Judgement judge(String path);

    /**
     * Convenience shortcut for {@code judge(path).verdict()}.
     *
     * @param path the variable path
     * @return the verdict of the judgement
     */
    default Verdict verdict(String path) {
        return judge(path).verdict();
    }

    /**
     * Narrow this schema to the sub-structure that lives at the given path.
     * <p>
     * This is what a local scope needs. When a command like {@code map} iterates over
     * {@code var("orders")} and evaluates its body against the individual elements, the body has to be
     * judged by {@code schema.at("orders[0]")} and not by the schema of the whole data.
     * <p>
     * The default implementation simply prefixes the path, which is correct for every schema whose
     * {@link #judge(String)} understands full paths.
     *
     * @param path the path of the sub-structure
     * @return the schema of the sub-structure
     */
    default Schema at(String path) {
        if (path == null || path.isEmpty()) {
            return this;
        }
        return sub -> judge(sub == null || sub.isEmpty() ? path : path + "." + sub);
    }

    /**
     * A schema that describes nothing and excludes nothing. This is the schema of a context that was
     * not given one, and it never triggers a {@link SchemaViolationException} in a lenient context.
     */
    Schema OPEN = path -> Judgement.unconstrained();
}

package com.javax0.logiqua.schema;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Schema;

import java.util.List;
import java.util.stream.Stream;

/**
 * A {@link Schema} described by the list of paths the data may contain.
 * <p>
 * A JSON Schema document is the right tool when one already exists. When it does not, writing one only
 * to catch mistyped variables is a lot of ceremony for a small job, and this class is the short way to
 * the same result: list the paths the scripts are allowed to read, and every other path becomes a
 * {@link com.javax0.logiqua.SchemaViolationException}.
 *
 * <pre>{@code
 * final var schema = PathSchema.of(
 *         "user.name",
 *         "user.address.*",
 *         "orders[*].total",
 *         "extras.**");
 * schema.verdict("user.name");            // DESCRIBED
 * schema.verdict("user");                 // DESCRIBED, an intermediate node of a described path
 * schema.verdict("orders[2].total");      // DESCRIBED
 * schema.verdict("user.nmae");            // IMPOSSIBLE
 * }</pre>
 * <p>
 * A pattern is a path in the syntax of {@link Context#get(String)} with two wildcards. A {@code *}
 * segment matches any single segment, an array index included, and a {@code **} segment matches every
 * remaining segment. A path that is a prefix of a pattern is described too, because reading the
 * intermediate node of a described path is legitimate; {@code var("user")} returns the whole object.
 * <p>
 * This schema never answers {@link Verdict#UNCONSTRAINED}. The list is the whole truth, which is why
 * {@code **} exists to open a subtree explicitly.
 */
public class PathSchema implements Schema {

    private final List<String[]> patterns;

    private PathSchema(List<String[]> patterns) {
        this.patterns = patterns;
    }

    /**
     * @param paths the allowed path patterns
     * @return the schema
     */
    public static PathSchema of(String... paths) {
        return of(List.of(paths));
    }

    /**
     * @param paths the allowed path patterns
     * @return the schema
     */
    public static PathSchema of(List<String> paths) {
        return new PathSchema(paths.stream().map(Context::segments).toList());
    }

    /**
     * @param paths the additional allowed path patterns
     * @return a new schema that describes the paths of this one and the given ones
     */
    public PathSchema and(String... paths) {
        return new PathSchema(Stream.concat(patterns.stream(), Stream.of(paths).map(Context::segments)).toList());
    }

    @Override
    public Judgement judge(String path) {
        if (path == null || path.isEmpty()) {
            return Judgement.described();
        }
        final var segments = Context.segments(path);
        for (final var pattern : patterns) {
            if (matches(pattern, 0, segments, 0)) {
                return Judgement.described();
            }
        }
        return Judgement.impossible("no described path of the schema starts with '" + path + "'");
    }

    /**
     * Match a path against a pattern, accepting a path that stops early because reading an
     * intermediate node of a described path is allowed.
     *
     * @param pattern       the pattern segments
     * @param patternIndex  the position in the pattern
     * @param path          the path segments
     * @param pathIndex     the position in the path
     * @return whether the pattern describes the path
     */
    private static boolean matches(String[] pattern, int patternIndex, String[] path, int pathIndex) {
        if (pathIndex == path.length) {
            return true;
        }
        if (patternIndex == pattern.length) {
            return false;
        }
        final var segment = pattern[patternIndex];
        if (segment.equals("**")) {
            return true;
        }
        if (segment.equals("*") || segment.equals(path[pathIndex])) {
            return matches(pattern, patternIndex + 1, path, pathIndex + 1);
        }
        return false;
    }
}

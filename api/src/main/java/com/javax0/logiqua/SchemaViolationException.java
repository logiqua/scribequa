package com.javax0.logiqua;

/**
 * Thrown when a script reads a variable that the {@link Schema} of the data proves can never exist.
 * <p>
 * This is deliberately not an {@link IllegalArgumentException}. An undefined variable is a run-time
 * condition that a script may legitimately handle, for example, with the two-argument form of
 * {@code var} that supplies a default. A schema violation is a bug in the script, and the default
 * value must not paper over it. Keeping the exception outside the {@code IllegalArgumentException}
 * hierarchy means the {@code catch} clauses that shrug off a missing variable do not swallow it.
 */
public class SchemaViolationException extends RuntimeException {

    private final String path;

    /**
     * @param path   the variable path that was read
     * @param reason why the schema rules this path out, may be {@code null}
     */
    public SchemaViolationException(String path, String reason) {
        super("The variable '" + path + "' cannot be defined by the data schema"
                + (reason == null || reason.isEmpty() ? "." : ": " + reason));
        this.path = path;
    }

    /**
     * @return the variable path whose reading triggered this exception
     */
    public String path() {
        return path;
    }
}

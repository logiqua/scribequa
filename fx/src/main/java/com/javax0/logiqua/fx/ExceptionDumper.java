package com.javax0.logiqua.fx;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class ExceptionDumper {
    public static String throwableToString(Throwable t) {
        StringBuilder sb = new StringBuilder();
        final var visited = Collections.newSetFromMap(new IdentityHashMap<>());
        appendThrowable(sb, t, "", visited);
        return sb.toString();
    }

    private static void appendThrowable(final StringBuilder sb,
                                        final Throwable t,
                                        final String prefix,
                                        final Set<Object> visited) {

        if (t == null) {
            return;
        }

        // Prevent infinite recursion
        if (visited.contains(t)) {
            sb.append(prefix)
                    .append("[CIRCULAR REFERENCE: ").append(t.getClass().getName()).append("]\n");
            return;
        }
        visited.add(t);

        // Print the exception header
        sb.append(prefix)
                .append(t.getClass().getName());
        if (t.getMessage() != null) {
            sb.append(": ").append(t.getMessage());
        }
        sb.append("\n");

        // Print stack trace elements
        for (StackTraceElement ste : t.getStackTrace()) {
            sb.append(prefix).append("    at ").append(ste).append("\n");
        }

        // Suppressed exceptions
        for (Throwable sup : t.getSuppressed()) {
            sb.append(prefix).append("  Suppressed: ");
            appendThrowable(sb, sup, prefix + "    ", visited);
        }

        // Cause
        Throwable cause = t.getCause();
        if (cause != null) {
            sb.append(prefix).append("Caused by: ");
            appendThrowable(sb, cause, prefix, visited);
        }
    }
}

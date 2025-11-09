package com.javax0.logiqua;

import java.util.Optional;

/**
 * An instance of the executor can execute a script.
 */
public interface Executor {
    /**
     * An executor always runs in a context. Some code wants to access the context directly.
     *
     * @return the context
     */
    Context getContext();

    /**
     * Returns the {@link Operation} associated with the specified symbol.
     * <p>
     * The engine loads the operations (commands and functions) when it is created using the
     * service loader mechanism.
     *
     * @param symbol the symbol representing the operation to retrieve
     * @return the operation matching the symbol, or null if not found
     */
    Optional<Operation> getOperation(String symbol);

    /**
     * Update an operation that was already loaded from the service loader mechanism.
     * <p>
     * The operation will replace the one operator in the engine that has the same symbol.
     *
     * @param operation the updating operation instance the engine has to use instead of the one loaded from the
     *                  service loader, or registered earlier
     */
    void updateOperation(Operation operation);
    void registerOperation(Operation operation);
    void registerOrUpdateOperation(Operation operation);
}

package com.javax0.logiqua.engine;

import com.javax0.logiqua.Operation;

import java.util.HashMap;
import java.util.Map;

/**
 * Hold a registry of all the operations.
 */
public class Registry {
    final private Map<String, Operation> operations = new HashMap<>();

    /**
     * Registers a new operation in the registry.
     * If an operation with the same symbol is already registered, an {@link IllegalArgumentException} is thrown.
     *
     * @param operation the operation to be registered. The operation must have a unique symbol
     *                  as defined by its {@code symbol()} method.
     *                  This symbol is used as the identifier in the registry.
     * @throws IllegalArgumentException if an operation with the same symbol is already registered.
     */
    public void register(Operation operation) {
        if (operations.containsKey(operation.symbol())) {
            throw new IllegalArgumentException("The operation " + operation.symbol() + " is already registered");
        }
        operations.put(operation.symbol(), operation);
    }

    /**
     * Updates an existing operation in the registry with a new definition.
     * The operation being updated must already be registered in the registry. If the operation is not found,
     * an {@link IllegalArgumentException} is thrown.
     * <p>
     * Use this method to manually override the definition of an operation that was already loaded from the service
     * loader mechanism.
     *
     * @param operation the operation to update. The operation must have a unique symbol as defined by its {@code symbol()} method,
     *                  and this symbol must already exist in the registry for the update to be successful.
     * @throws IllegalArgumentException if the operation's symbol is not found in the registry.
     */
    public void update(Operation operation) {
        if (!operations.containsKey(operation.symbol())) {
            throw new IllegalArgumentException("The operation " + operation.symbol() + " is not registered, cannot be updated");
        }
        operations.put(operation.symbol(), operation);
    }

    public Operation get(String symbol) {
        if (!operations.containsKey(symbol)) {
            throw new IllegalArgumentException("The operation '" + symbol + "' is not registered");
        }
        return operations.get(symbol);
    }

}

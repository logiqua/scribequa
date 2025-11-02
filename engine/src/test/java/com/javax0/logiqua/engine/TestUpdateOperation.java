package com.javax0.logiqua.engine;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * The TestUpdateOperation class contains a test that verifies the functionality of updating
 * and executing operations in an Engine instance. The test defines and registers a custom
 * function operation, executes it within an Engine context, and validates its behavior.
 * <p>
 * This test demonstrates the following:
 * 1. Creating an Engine instance with an empty data context.
 * 2. Defining a custom Operation.Function to handle specific logic, such as appending strings to a log.
 * 3. Registering the custom operation in the Engine using the updateOperation method.
 * 4. Executing the custom operation through a dynamically created script.
 * 5. Verifying the operation's execution by asserting the modified log's content.
 */
public class TestUpdateOperation {
    private static final String LOG_SYMBOL = "log";

    @Test
    void testLogUpdate() {
        final var log = new StringBuilder();
        final var engine = Engine.withData(Map.of());

        final var logFunction = new Operation.Function() {

            @Override
            public String symbol() {
                return LOG_SYMBOL;
            }

            @Override
            public Object evaluate(Executor executor, Object... args) {
                log.append(args[0].toString());
                return null;
            }
        };
        engine.updateOperation(logFunction);
        final var script =
                engine.getOp(LOG_SYMBOL).args(
                        engine.constant("Hello, World!"));
        script.evaluate();
        Assertions.assertEquals("Hello, World!", log.toString());
    }

    @Test
    void testNewCommand() {
        final var log = new StringBuilder();
        final var engine = Engine.withData(Map.of());
        final var symbol = this.getClass().getCanonicalName() + ".testNewCommand";
        final var logFunction = new Operation.Function() {

            @Override
            public String symbol() {
                return symbol;
            }

            @Override
            public Object evaluate(Executor executor, Object... args) {
                log.append(args[0].toString());
                return null;
            }
        };
        engine.registerOperation(logFunction);
        final var script =
                engine.getOp(symbol).args(
                        engine.constant("Hello, World!"));
        script.evaluate();
        Assertions.assertEquals("Hello, World!", log.toString());
    }
}

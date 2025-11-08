package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.jsonlogic.JsonLogic;

@Named.Symbol("or")
@Operation.Limited(min = 1)
public class JLOr implements Operation.Command {
    @Override
    public Object evaluate(Executor executor, Script... args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("The or command requires at least one argument.");
        }
        Object result = null;

        for (final var arg : args) {
            result = arg.evaluate();

            if (JsonLogic.truthy(result)) {
                return result;
            }
        }
        return result;
    }
}

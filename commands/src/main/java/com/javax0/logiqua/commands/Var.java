package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

@Named.Symbol("var")
@Operation.Limited(min = 1, max = 2)
public class Var implements Operation.Function {

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var key = args[0].toString();

        final var value = executor.getContext().get(args[0].toString());
        if (value == null) {
            throw new IllegalArgumentException("The variable " + key + " is not defined.");
        }
        return value.get();
    }


}

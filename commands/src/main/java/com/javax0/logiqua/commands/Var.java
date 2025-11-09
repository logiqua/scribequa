package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Castor;

@Named.Symbol("var")
@Operation.Arity(min = 1, max = 2)
public class Var implements Operation.Function {

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var cast = new Castor(executor);
        final var key = cast.toString(args[0])
                .orElseThrow(() -> new IllegalArgumentException("The first argument of the var command must be a string."));

        final var value = executor.getContext().get(key);
        if (value == null) {
            if (args.length == 2) {
                return args[1];
            } else {
                throw new IllegalArgumentException("The variable " + key + " is not defined.");
            }
        }
        return value.get();
    }
}

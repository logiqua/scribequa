package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Castor;

@Operation.Arity(min = 1, max = 1)
@Operation.Symbol("!")
public class Not implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var cast = new Castor(executor);
        final var value = cast.toBoolean(args[0])
                .orElseThrow(() -> new IllegalArgumentException("The argument of the not command must be a boolean expression."));
        return !value;
    }
}

package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.util.Objects;

@Operation.Limited(min = 1, max = 1)
@Operation.Symbol("!")
public class Not implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var value = executor.getContext().caster(Context.classOf(args[0]), Boolean.class)
                .orElseThrow(() -> new IllegalArgumentException("The argument of the not command must be a boolean expression."))
                .cast(args[0]);
        return !value;
}
}

package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.util.Objects;

@Operation.Limited(min = 2)
@Operation.Symbol("==")
public class Equals implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var base = args[0];
        for (int i = 1; i < args.length; i++) {
            final var value = executor.getContext().caster(Context.classOf(args[i]),Context.classOf(base))
                    .orElseThrow(() -> new IllegalArgumentException("The arguments of the equals command must be of the same type after coercion."))
                    .cast(args[i]);
            if( !Objects.equals(base, value) ){
                return false;
            }
        }
        return true;
    }
}

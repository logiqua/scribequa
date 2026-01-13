package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Equals;

import java.util.Objects;

@Operation.Arity(min = 2)
@Operation.Symbol("!=")
public class NotEquals implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var base = args[0];
        for (int i = 1; i < args.length; i++) {
            final var caster = executor.getContext().caster(Context.classOf(args[i]), Context.classOf(base));
            if (caster.isEmpty()) {
                return false;
            }
            final var value = caster.get().cast(args[i]);
            if (Equals.equals(base, value)) {
                return false;
            }
        }
        return true;
    }
}

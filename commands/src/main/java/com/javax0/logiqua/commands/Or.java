package com.javax0.logiqua.commands;

import com.javax0.logiqua.*;

import java.util.HashSet;
import java.util.Set;

@Named.Symbol("or")
@Operation.Limited(min = 2)
public class Or implements Operation.Command {

    @Override
    public Set<Class<?>> returns(Script... args) {
        return Set.of(Boolean.class);
    }

    @Override
    public Object evaluate(Executor executor, Script... args) {
        for( int i = 0; i < args.length; i++) {
            final var argi = args[i].evaluate();
            final var term = executor.getContext().caster(Context.classOf(argi), Boolean.class)
                    .orElseThrow(() -> new IllegalArgumentException("The first argument of the 'or' command must be a boolean expression."))
                    .cast(argi);
            if (term) {
                return true;
            }
        }
        return false;
    }
}

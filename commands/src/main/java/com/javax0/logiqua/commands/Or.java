package com.javax0.logiqua.commands;

import com.javax0.logiqua.*;

@Named.Symbol("or")
@Operation.Arity(min = 2)
public class Or implements Operation.Macro {

    @Override
    public Object evaluate(Executor executor, Script... args) {
        for( final var arg : args) {
            final var evaluated = arg.evaluate();
            final var term = executor.getContext().caster(Context.classOf(evaluated), Boolean.class)
                    .orElseThrow(() -> new IllegalArgumentException("The first argument of the 'or' command must be a boolean expression."))
                    .cast(evaluated);
            if (term) {
                return true;
            }
        }
        return false;
    }
}

package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.commands.utils.Castor;

@Named.Symbol("and")
@Operation.Arity(min = 2)
public class And implements Operation.Macro {

    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var cast = new Castor(executor);
        for (final var arg : args) {
            final var evaluated = arg.evaluate();
            final var term = cast.toBoolean(evaluated)
                    .orElseThrow(() -> new IllegalArgumentException("The arguments of the 'and' command must be a boolean expression."));
            if (!term) {
                return false;
            }
        }
        return true;
    }
}

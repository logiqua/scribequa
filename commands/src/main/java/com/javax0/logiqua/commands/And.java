package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.commands.utils.Castor;

import java.util.Set;

@Named.Symbol("and")
@Operation.Arity(min = 2)
public class And implements Operation.Macro {

    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var cast = new Castor(executor);
        for (int i = 0; i < args.length; i++) {
            final var argi = args[i].evaluate();
            final var term = cast.toBoolean(argi)
                    .orElseThrow(() -> new IllegalArgumentException("The arguments of the 'and' command must be a boolean expression."));
            if (!term) {
                return false;
            }
        }
        return true;
    }
}

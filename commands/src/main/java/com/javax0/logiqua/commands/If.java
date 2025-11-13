package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.commands.utils.Castor;

@Named.Symbol("if")
@Operation.Arity(min = 2, max = 3)
public class If implements Operation.Macro {

    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var cast = new Castor(executor);
        final var arg0 = args[0].evaluate();

        if (cast.toBoolean(arg0)
                .orElseThrow(() -> new IllegalArgumentException("The first argument of the 'if' command must be a boolean expression."))) {
            return args[1].evaluate();
        } else if (args.length > 2) {
            return args[2].evaluate();
        } else {
            return null;
        }
    }
}

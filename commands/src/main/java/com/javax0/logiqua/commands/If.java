package com.javax0.logiqua.commands;

import com.javax0.logiqua.*;

import java.util.HashSet;
import java.util.Set;

@Named.Symbol("if")
@Operation.Limited(min = 2, max = 3)
public class If implements Operation.Command {

    @Override
    public Set<Class<?>> returns(Script... args) {
        final var retvals = new HashSet<Class<?>>();
        retvals.addAll(args[0].returns());
        retvals.addAll(args[1].returns());
        return retvals;
    }

    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var arg0 = args[0].evaluate();
        final var caster = executor.getContext().caster(Context.classOf(arg0), Boolean.class)
                .orElseThrow(() -> new IllegalArgumentException("The first argument of the if command must be a boolean expression."));

        final var condition = caster.cast(arg0);
        if (condition) {
            return args[1].evaluate();
        }
        if (args.length > 2) {
            return args[2].evaluate();
        }
        return null;
    }
}

package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Array;

import java.util.ArrayList;
import java.util.List;

@Named.Symbol("missing_some")
@Operation.Limited(min = 2)
public class MissingSome implements Operation.Function {
    final static Missing missing = new Missing();

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var required = executor.getContext().caster(Context.classOf(args[0]), Integer.class)
                .orElseThrow(() -> new IllegalArgumentException("The first argument of the missing_some command must be an integer."))
                .cast(args[0]);
        final var missing = new ArrayList<String>();
        final Object[] arguments;
        if (args.length == 2) {
            arguments = new Array(executor).flat(args[1]);
        } else {
            arguments = args;
        }

        int counter = 0;
        for (final var arg : arguments) {
            if (arg != null) {
                final var key = arg.toString();
                final var value = executor.getContext().get(key);
                if (value == null) {
                    missing.add(key);
                } else {
                    counter++;
                }
                if (counter >= required) {
                    return List.of();
                }
            }
        }
        return missing;
    }
}

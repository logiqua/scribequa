package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Array;

import java.util.ArrayList;

@Named.Symbol("missing")
public class Missing implements Operation.Function {

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var missing = new ArrayList<String>();
        final Object[] arguments;
        if (args.length == 1) {
            arguments = new Array(executor).flat(args[0]);
        }else{
            arguments = args;
        }
        for (final var arg : arguments) {
            if (arg != null) {
                final var key = arg.toString();
                final var value = executor.getContext().get(key);
                if (value == null) {
                    missing.add(key);
                }
            }
        }
        return missing;
    }

}

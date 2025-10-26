package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.util.ArrayList;

@Named.Symbol("missing")
public class Missing implements Operation.Function {

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var missing = new ArrayList<String>();
        if (args.length == 1) {
            final var list = args[0];
            if (executor.getContext().accessor(list) instanceof Context.ListLike listAccessor) {
                final var size = listAccessor.size(list);
                for (int i = 0; i < size; i++) {
                    final var arg = listAccessor.get(list, i);
                    if (arg != null && arg.get() != null) {
                        final var key = arg.get().toString();
                        final var value = executor.getContext().get(key);
                        if (value == null) {
                            missing.add(key);
                        }
                    }
                }
                return missing;
            }
        }
        for (final var arg : args) {
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

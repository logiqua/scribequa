package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.util.ArrayList;

@Named.Symbol("missing_some")
@Operation.Limited(min = 2)
public class MissingSome implements Operation.Function {

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var missing = new ArrayList<String>();
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

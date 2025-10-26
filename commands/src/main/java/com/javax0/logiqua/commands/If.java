package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;

import java.util.HashSet;
import java.util.Set;

@Named.Symbol("if")
@Operation.Limited(min = 2, max = 3)
public class If implements Operation.Command {


    @Override
    public void checkArguments(Script... args) {
        Operation.Command.super.checkArguments(args);
        final var firstArgType = args[0].returns();
        if (!firstArgType.contains(Boolean.class) && !firstArgType.contains(boolean.class) && (firstArgType != NOT_IMPLEMENTED)) {
            throw new IllegalArgumentException("The first argument of the if command must be a boolean expression.");
        }
    }

    @Override
    public Set<Class<?>> returns(Script... args) {
        final var retvals = new HashSet<Class<?>>();
        retvals.addAll(args[0].returns());
        retvals.addAll(args[1].returns());
        return retvals;
    }

    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var condition = args[0].evaluate(executor);
        if (condition instanceof Boolean bool) {
            if (bool) {
                return args[1].evaluate(executor);
            }
            if (args.length > 2) {
                return args[2].evaluate(executor);
            }
            return null;
        }else{
            throw new IllegalArgumentException("The first argument of the if command must be a boolean expression.");
        }
    }
}

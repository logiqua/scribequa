package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Castor;

@Operation.Symbol("cat")
@Operation.Arity(min = 1)
public class Cat implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var sb = new StringBuilder();
        final var cast = new Castor(executor);
        for (final var arg : args) {
            sb.append(cast.toString(arg)
                    .orElseThrow(() -> new IllegalArgumentException("The arguments of the cat command must be strings.")));
        }
        return sb.toString();
    }
}

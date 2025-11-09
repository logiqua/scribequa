package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

@Named.Symbol("!==")
@Operation.Arity(min = 2, max = 2)
public class JLStrictInEqual implements Operation.Function{
    private final JLStrictEqual delegate = new JLStrictEqual();

    @Override
    public Object evaluate(Executor executor, Object... args) {
        return !delegate.evaluate(executor, args);
    }
}

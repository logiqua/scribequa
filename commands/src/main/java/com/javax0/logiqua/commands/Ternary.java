package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;

@Named.Symbol("?:")
@Operation.Arity(min = 2, max = 3)
public class Ternary implements Operation.Macro {

    private final If delegate = new If();

    @Override
    public Object evaluate(Executor executor, Script... args) {
        return delegate.evaluate(executor,args);
    }
}

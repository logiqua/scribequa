package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.Multiply;

@Named.Symbol("*")
@Operation.Arity(min = 1)
public class JLMultiply implements Operation.Function {
    private final Multiply delegate = new Multiply();

    @Override
    public Object evaluate(Executor executor, Object... arguments) {
        final Object[] args;
        if (arguments.length == 1) {
            args = new Object[]{arguments[0], 1L};
        }else{
            args = arguments;
        }
        return delegate.evaluate(executor, args);
    }
}

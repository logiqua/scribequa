package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

@Named.Symbol("in")
public class JLIn implements Operation.Function{
    private final com.javax0.logiqua.commands.In delegate = new com.javax0.logiqua.commands.In();

    @Override
    public Object evaluate(Executor executor, Object... args) {
        if( args.length < 2 ){
            return false;
        }
        final var containedIn = args[1];
        if( containedIn == null ){
            return false;// nothing is contained in null
        }

        return delegate.evaluate(executor, args);
    }
}

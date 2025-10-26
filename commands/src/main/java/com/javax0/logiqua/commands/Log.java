package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

@Named.Symbol("log")
public class Log implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        for( final var arg : args ){
            System.out.println(""+arg);
        }
        return null;
    }
}

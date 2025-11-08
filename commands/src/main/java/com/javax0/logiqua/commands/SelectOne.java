package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.util.function.BiFunction;

abstract class SelectOne implements Operation.Function {
    abstract BiFunction<Object, Object, Object> selector();

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var base = Context.classOf(args[0]);
        var selected = args[0];
        for (int i = 1; i < args.length; i++) {
            selected = selector().apply(selected,args[i]);
        }
        return selected;
    }
}

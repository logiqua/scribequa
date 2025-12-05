package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.util.ArrayList;
import java.util.Arrays;

@Operation.Symbol("it")
public class It implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var it = new ArrayList<>(args.length);
        it.addAll(Arrays.asList(args));
        return it;
    }
}

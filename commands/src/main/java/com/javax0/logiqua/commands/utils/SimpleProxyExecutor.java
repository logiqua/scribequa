package com.javax0.logiqua.commands.utils;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

public class SimpleProxyExecutor implements Executor {

    private final Executor executor;
    private final Context context;

    public SimpleProxyExecutor(Executor executor, Context context) {
        this.executor = executor;
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Operation getOperation(String symbol) {
        return executor.getOperation(symbol);
    }

    @Override
    public void updateOperation(Operation operation) {
        executor.updateOperation(operation);
    }

    @Override
    public void registerOperation(Operation operation) {
        executor.registerOperation(operation);
    }

    @Override
    public void registerOrUpdateOperation(Operation operation) {
        executor.registerOrUpdateOperation(operation);
    }
}

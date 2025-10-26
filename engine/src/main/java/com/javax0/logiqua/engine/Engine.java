package com.javax0.logiqua.engine;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.scripts.ConstantValueNode;

import java.util.Map;
import java.util.ServiceLoader;

public class Engine implements Executor, Builder {
    private final Context context;
    private final Registry registry = new Registry();

    public static Engine withData(Map<String, Object> map) {
        return withData(new MapContext(map));
    }

    public static Engine withData(Context context) {
        return new Engine(context);
    }

    private Engine(Context context) {
        this.context = context;
        loadCommandsAndFunctions();
    }

    /**
     * Load all the functions and commands from the service loader.
     */
    private void loadCommandsAndFunctions() {
        ServiceLoader.load(Operation.Function.class).forEach(registry::register);
        ServiceLoader.load(Operation.Command.class).forEach(registry::register);
    }


    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Operation getOperation(String symbol) {
        return registry.get(symbol);
    }

    @Override
    public void updateOperation(Operation operation) {
        registry.update(operation);
    }

    @Override
    public NodeBuilder getOp(String symbol) {
        return switch (getOperation(symbol)) {
            case Operation.Command command -> new CommandNodeBuilder(this, command);
            case Operation.Function function -> new FunctionNodeBuilder(this, function);
        };
    }

    @Override
    public ConstantValueNode<?> constant(Object value) {
        return new ConstantValueNode<>(value);
    }

}

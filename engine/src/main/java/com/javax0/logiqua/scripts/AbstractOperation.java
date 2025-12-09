package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;

import java.util.Arrays;
import java.util.stream.Collectors;

sealed abstract class AbstractOperation implements Script permits FunctionNode, MacroNode {
    protected final Operation operation;
    protected final Script[] args;
    protected final Engine engine;

    public AbstractOperation(final Engine engine, final Operation operation, final Script... args) {
        this.engine = engine;
        this.operation = operation;
        this.args = args;
    }


    @Override
    public String jsonify() {
        return "{\"" + operation.symbol() + "\":[" + Arrays.stream(args).map(Script::jsonify).collect(Collectors.joining(",")) + "]}";
    }
}

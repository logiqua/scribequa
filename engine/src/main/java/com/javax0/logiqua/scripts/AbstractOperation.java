package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;

import java.util.Set;

sealed abstract class AbstractOperation implements Script permits FunctionNode, CommandNode {
    protected final com.javax0.logiqua.Operation operation;
    protected final Script[] args;
    protected final Engine engine;

    public AbstractOperation(final Engine engine, com.javax0.logiqua.Operation operation, Script... args) {
        this.engine = engine;
        this.operation = operation;
        this.args = args;
    }

    public Set<Class<?>> returns() {
        return operation.returns(args);
    }
}

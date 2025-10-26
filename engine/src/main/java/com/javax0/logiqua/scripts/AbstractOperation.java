package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Script;

import java.util.Set;

sealed abstract class AbstractOperation implements Script permits FunctionNode, CommandNode {
    protected final com.javax0.logiqua.Operation operation;
    protected final Script[] args;

    public AbstractOperation(com.javax0.logiqua.Operation operation, Script... args) {
        this.operation = operation;
        this.args = args;
    }

    public Set<Class<?>> returns() {
        return operation.returns(args);
    }
}

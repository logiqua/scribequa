package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;

non-sealed public class CommandNode extends AbstractOperation {

    public CommandNode(com.javax0.logiqua.Operation.Command operation, Script... args) {
        super(operation, args);
    }

    @Override
    public Object evaluate(Executor executor) {
        return ((Operation.Command) super.operation).evaluate(executor, args);
    }
}

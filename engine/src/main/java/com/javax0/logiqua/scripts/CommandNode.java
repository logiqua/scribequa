package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;

non-sealed public class CommandNode extends AbstractOperation {

    public CommandNode(Engine engine, com.javax0.logiqua.Operation.Command operation, Script... args) {
        super(engine, operation, args);
    }

    @Override
    public Object evaluate() {
        return evaluateUsing(engine);
    }

    @Override
    public Object evaluateUsing(Executor executor) {
        return ((Operation.Command) super.operation).evaluate(executor, args);
    }
}

package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;

import java.util.Arrays;

non-sealed public class FunctionNode extends AbstractOperation {


    public FunctionNode(Engine engine, com.javax0.logiqua.Operation.Function operation, Script... args) {
        super(engine, operation, args);
    }

    @Override
    public Object evaluate() {
        return evaluateUsing(engine);
    }

    @Override
    public Object evaluateUsing(Executor executor) {
        return ((Operation.Function) operation).evaluate(executor, Arrays.stream(args).map(s -> s.evaluateUsing(executor)).toArray());
    }
}

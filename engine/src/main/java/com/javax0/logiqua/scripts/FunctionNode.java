package com.javax0.logiqua.scripts;

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
        return ((Operation.Function) operation).evaluate(engine, Arrays.stream(args).map(Script::evaluate).toArray());
    }
}

package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;

import java.util.Arrays;

non-sealed public class FunctionNode extends AbstractOperation {


    public FunctionNode(com.javax0.logiqua.Operation.Function operation, Script... args) {
        super(operation, args);
    }

    @Override
    public Object evaluate(Executor executor) {
        return ((Operation.Function)operation).evaluate(executor, Arrays.stream(args).map(arg -> arg.evaluate(executor)).toArray());
    }
}

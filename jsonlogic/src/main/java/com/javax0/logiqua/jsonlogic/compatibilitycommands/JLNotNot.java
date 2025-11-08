package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.jsonlogic.JsonLogic;

@Named.Symbol("!!")
public class JLNotNot implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        boolean result;

        if (args.length == 0) {
            result = false;
        }
        else {
            result = JsonLogic.truthy(args[0]);
        }
        return result;
    }
}

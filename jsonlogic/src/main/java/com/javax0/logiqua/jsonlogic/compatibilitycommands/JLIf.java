package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.jsonlogic.JsonLogic;

@Named.Symbol("if")
public class JLIf implements Operation.Command {
    @Override
    public Object evaluate(Executor executor, Script... args) {
        if (args.length < 1) {
            return null;
        }

        // If there is only a single argument, simply evaluate & return that argument.
        if (args.length == 1) {
            return args[0].evaluate();
        }

        // If there are 2 arguments, only evaluate the second argument if the first argument is truthy.
        if (args.length == 2) {
            return JsonLogic.truthy(args[0].evaluate())
                    ? args[1].evaluate()
                    : null;
        }

        for (int i = 0; i < args.length - 1; i += 2) {
            final var condition = args[i];
            final var resultIfTrue = args[i + 1];

            if (JsonLogic.truthy(condition.evaluate())) {
                return resultIfTrue.evaluate();
            }
        }

        if ((args.length & 1) == 0) {
            return null;
        }

        return args[args.length - 1].evaluate();
    }
}

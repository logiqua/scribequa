package com.javax0.logiqua.commands;

import com.javax0.logiqua.*;
import com.javax0.logiqua.commands.utils.LocalExecutor;

import java.util.ArrayList;
import java.util.HashMap;

@Named.Symbol("reduce")
@Operation.Limited(min = 3, max = 3)
public class Reduce implements Operation.Command {
    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var accessor = executor.getContext().accessor(args[0].evaluate());
        if (!(accessor instanceof Context.Indexed inList)) {
            throw new IllegalArgumentException("The first argument of the reduce command must be a list.");
        }
        final var script = args[1];
        var accumulator = args[2].evaluate();
        final var map = new HashMap<String, Object>();
        final var loopExecutor = LocalExecutor.of(map, executor);
        for (int i = 0; i < inList.size(); i++) {
            final var item = inList.get(i);
            map.put("current", item.get());
            map.put("", item.get());
            map.put("accumulator", accumulator);
            accumulator = script.evaluateUsing(loopExecutor);
            }
        return accumulator;
    }
}

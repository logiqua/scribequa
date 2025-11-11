package com.javax0.logiqua.commands;

import com.javax0.logiqua.*;
import com.javax0.logiqua.commands.utils.LocalExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Named.Symbol("reduce")
@Operation.Arity(min = 3, max = 3)
public class Reduce implements Operation.Macro {
    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var list = Objects.requireNonNullElse(args[0].evaluate(), List.of());
        final var accessor = executor.getContext().accessor(list);
        if (!(accessor instanceof Context.IndexedProxy inList)) {
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

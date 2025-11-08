package com.javax0.logiqua.commands;

import com.javax0.logiqua.*;
import com.javax0.logiqua.commands.utils.Castor;
import com.javax0.logiqua.commands.utils.LocalExecutor;

import java.util.HashMap;

@Named.Symbol("some")
@Operation.Limited(min = 2, max = 2)
public class Some implements Operation.Command {
    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var accessor = executor.getContext().accessor(args[0].evaluate());
        if (!(accessor instanceof Context.Indexed inList)) {
            throw new IllegalArgumentException("The first argument of the 'some' command must be a list.");
        }
        final var script = args[1];
        final var map = new HashMap<String, Object>();
        final var loopExecutor = LocalExecutor.of(map, executor);
        final var cast = new Castor(loopExecutor);
        for (int i = 0; i < inList.size(); i++) {
            final var item = inList.get(i);
            map.put("", item.get());
            final var filtered = script.evaluateUsing(loopExecutor);
            if (cast.toBoolean(filtered)
                    .orElseThrow(() -> new IllegalArgumentException("The all script must return a boolean value."))) {
                return true;
            }
        }
        return false;
    }
}

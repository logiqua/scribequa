package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.*;
import com.javax0.logiqua.commands.utils.Castor;
import com.javax0.logiqua.commands.utils.LocalExecutor;

import java.util.ArrayList;
import java.util.HashMap;

@Named.Symbol("filter")
@Operation.Arity(min = 2, max = 2)
public class JLFilter implements Operation.Macro {
    @Override
    public Object evaluate(Executor executor, Script... args) {
        final var accessor = executor.getContext().accessor(args[0].evaluate());
        if (!(accessor instanceof Context.IndexedProxy inList)) {
            throw new IllegalArgumentException("The first argument of the filter command must be a list.");
        }
        final var script = args[1];
        final var outList = new ArrayList<>();
        final var map = new HashMap<String, Object>();
        final var loopExecutor = LocalExecutor.of(map, executor);
        final var cast = new Castor(loopExecutor);
        for (int i = 0; i < inList.size(); i++) {
            final var item = inList.get(i);
            map.put("current", item.get());
            map.put("", item.get());
            final var filtered = script.evaluateUsing(loopExecutor);
            if (cast.toBoolean(filtered)
                    .orElseThrow(() -> new IllegalArgumentException("The filter script must return a boolean value."))) {
                outList.add(item.get());
            }
        }
        return outList;
    }
}

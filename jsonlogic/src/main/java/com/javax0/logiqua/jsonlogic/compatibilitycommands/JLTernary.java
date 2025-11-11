package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;

@Named.Symbol("?:")
public class JLTernary implements Operation.Macro {
    private final JLIf delegate = new JLIf();

    @Override
    public Object evaluate(Executor executor, Script... args) {
        return delegate.evaluate(executor, args);
    }
}

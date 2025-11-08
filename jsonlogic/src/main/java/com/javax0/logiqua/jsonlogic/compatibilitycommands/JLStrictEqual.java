package com.javax0.logiqua.jsonlogic.compatibilitycommands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

@Named.Symbol("===")
@Operation.Limited(min = 2, max = 2)
public class JLStrictEqual implements Operation.Function{
    @Override
    public Boolean evaluate(Executor executor, Object... args) {
        final var left = args[0];
        final var right = args[1];

        if (left instanceof Number && right instanceof Number) {
            return ((Number) left).doubleValue() == ((Number) right).doubleValue();
        }

        if (left == right) {
            return true;
        }

        return left != null && left.equals(right);

    }
}

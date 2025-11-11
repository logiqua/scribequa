package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.util.function.BiFunction;

@Operation.Arity(min = 1)
@Named.Symbol("min")
public class Min extends SelectOne {
    @Override
    BiFunction<Object, Object, Object> selector() {
        return  (a,b) -> {
            @SuppressWarnings("unchecked")
            var comparable = (Comparable<Object>)a;
            return comparable.compareTo(b) < 0 ? a : b;
        };
    }
}

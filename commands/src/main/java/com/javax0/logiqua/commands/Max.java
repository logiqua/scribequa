package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

@Operation.Limited(min = 2)
@Named.Symbol("max")
public class Max extends SelectOne {
    @Override
    BiFunction<Object, Object, Object> selector() {
        return  (a,b) -> {
            @SuppressWarnings("unchecked")
            var comparable = (Comparable<Object>)a;
            return comparable.compareTo(b) < 0 ? b : a;
        };
    }
}

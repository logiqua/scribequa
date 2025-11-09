package com.javax0.logiqua.commands;

import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.util.function.BiPredicate;

@Operation.Arity(min = 2)
@Named.Symbol("<=")
public class LessThanOrEqual extends Between {
    @Override
    BiPredicate<Comparable<?>, Comparable<?>> comparator() {
        return (a,b) -> {
            @SuppressWarnings("unchecked")
            var comparable = (Comparable<Object>)a;
            return comparable.compareTo(b) <= 0;
        };

    }
}

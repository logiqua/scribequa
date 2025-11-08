package com.javax0.logiqua.commands.utils;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;

import java.util.Optional;

public class Castor {
    private final Executor executor;

    public Castor(Executor executor) {
        this.executor = executor;
    }

    private <T> Optional<T> cast(Object object, Class<T> type) {
        return executor.getContext().caster(Context.classOf(object), type).map(c -> c.cast(object));
    }

    public Optional<String> toString(Object object) {
        return cast(object, String.class);
    }

    public Optional<Boolean> toBoolean(Object object) {
        return cast(object, Boolean.class);
    }

    public Optional<Long> toLong(Object object) {
        return cast(object, Long.class);
    }

    public Optional<Double> toDouble(Object object) {
        return cast(object, Double.class);
    }

}

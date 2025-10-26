package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Script;

import java.util.Set;

public class ConstantValueNode<T> implements Script {

    private final T value;

    public ConstantValueNode(T value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Executor executor) {
        return value;
    }

    @Override
    public Set<Class<?>> returns() {
        return Set.of(value.getClass());
    }
}
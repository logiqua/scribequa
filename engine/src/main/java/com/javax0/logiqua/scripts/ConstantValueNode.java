package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Script;

public class ConstantValueNode<T> implements Script {

    private final T value;

    public ConstantValueNode(T value) {
        this.value = value;
    }

    @Override
    public Object evaluate() {
        return value;
    }

    @Override
    public Object evaluateUsing(Executor executor) {
        return value;
    }
}
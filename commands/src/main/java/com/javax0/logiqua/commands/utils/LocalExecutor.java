package com.javax0.logiqua.commands.utils;

import com.javax0.logiqua.Executor;

import java.util.Map;

public class LocalExecutor {

    public static Executor of(Map<String, Object> map, Executor executor) {
        final var loopContext = new SimpleProxyMapContext(map, executor.getContext());
        return new SimpleProxyExecutor(executor, loopContext);
    }

}

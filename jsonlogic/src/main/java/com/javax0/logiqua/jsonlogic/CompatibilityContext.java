package com.javax0.logiqua.jsonlogic;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.engine.MapContext;

import java.util.HashMap;
import java.util.Optional;

public class CompatibilityContext implements Context {
    final MapContext mapContext;

    public CompatibilityContext(Object data) {
        final var map = new HashMap<String, Object>();
        map.put("data", data);
        this.mapContext = new MapContext(map);
    }

    @Override
    public Value get(String key) {
        return mapContext.get("data." + key);
    }

    @Override
    public Proxy accessor(Object target) {
        return mapContext.accessor(target);
    }

    @Override
    public <From, To> Optional<Caster<From, To>> caster(Class<From> from, Class<To> to) {
        return mapContext.caster(from, to);
    }
}

package com.javax0.logiqua.json;

import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.scripts.ConstantValueNode;

import java.util.List;
import java.util.Map;

public class JsonBuilder {

    private final Object jsonObject;
    private final Engine engine;

    public static JsonBuilder from(Object jsonObject, Engine engine) {
        return new JsonBuilder(jsonObject, engine);
    }

    private JsonBuilder(Object jsonObject, Engine engine) {
        this.jsonObject = jsonObject;
        this.engine = engine;
    }

    public Script build() {
        if (jsonObject instanceof List<?> list) {
            return build(Map.of("it", list));
        } else {
            return build(jsonObject);
        }
    }

    private Script build(Object jsonObject) throws IllegalArgumentException {
        if (jsonObject instanceof Map<?, ?> map) {
            if (map.size() != 1) {
                return new ConstantValueNode<>(jsonObject);
            }
            final var entry = map.entrySet().iterator().next();
            final var key = entry.getKey();
            if (!(key instanceof String keyString)) {
                throw new IllegalArgumentException("The command, which is the key of the map must be a string");
            }
            final var nodeBuilder = engine.getOp(keyString);
            final var value = entry.getValue();
            if (value instanceof List<?> listValue) {
                return nodeBuilder.subscripts(listValue.stream().map(this::build).toArray(Script[]::new));
            } else {
                return nodeBuilder.subscripts(build(value));
            }
        } else if (jsonObject instanceof List<?> list) {
            return build(Map.of("it", list));
        } else {
            return new ConstantValueNode<>(jsonObject);
        }
    }
}

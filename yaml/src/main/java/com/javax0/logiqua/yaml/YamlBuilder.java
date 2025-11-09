package com.javax0.logiqua.yaml;

import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.scripts.ConstantValueNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlBuilder {

    private final Object yamlObject;
    private final Engine engine;

    public static YamlBuilder from(Object yamlObject, Engine engine) {
        return new YamlBuilder(yamlObject, engine);
    }

    private YamlBuilder(Object yamlObject, Engine engine) {
        this.yamlObject = yamlObject;
        this.engine = engine;
    }

    public Script build() {
        return build(yamlObject);
    }

    private Script build(Object yamlObject) throws IllegalArgumentException {
        if (yamlObject instanceof Map<?, ?> map) {
            if (map.size() != 1) {
                return new ConstantValueNode<>(yamlObject);
            }
            final var entry = map.entrySet().iterator().next();
            final var key = entry.getKey();
            if (!(key instanceof String keyString)) {
                throw new IllegalArgumentException("The command, which is the key of the map must be a string");
            }
            final var nodeBuilder = engine.getOp(keyString);
            final var args = entry.getValue();
            final var scripts = new ArrayList<Script>();
            if (args instanceof List<?> listValue) {
                for (final var arg : listValue) {
                    scripts.add(build(arg));
                }
            } else {
                scripts.add(build(args));
            }
            return nodeBuilder.subscripts(scripts.toArray(Script[]::new));
        }
        return new ConstantValueNode<>(yamlObject);
    }
}

package com.javax0.logiqua.yaml;

import com.javax0.logiqua.Logiqua;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;

import java.util.Map;

public class YamlLogiqua implements Logiqua {

    private Engine engine = null;

    public Engine engine() {
        return engine;
    }

    public YamlLogiqua with(Engine engine) {
        this.engine = engine;
        return this;
    }

    public YamlLogiqua with(Map<String, Object> data) {
        if (engine != null) {
            throw new IllegalStateException("The engine is already set");
        }
        this.engine = Engine.withData(data);

        return this;
    }

    @Override
    public Script compile(String source) {
        if (engine == null) {
            engine = Engine.withData(Map.of());
        }
        if( engine.limit() < source.length()) {
            throw new IllegalArgumentException("The source is too long");
        }

        final var yaml = YamlReader.of(source).read();


        return YamlBuilder.from(yaml, engine).build();
    }
}

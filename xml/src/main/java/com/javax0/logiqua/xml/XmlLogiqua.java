package com.javax0.logiqua.xml;

import com.javax0.logiqua.Logiqua;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;

import java.util.Map;

public class XmlLogiqua implements Logiqua {

    private Engine engine = null;

    public Engine engine() {
        return engine;
    }

    public XmlLogiqua with(Engine engine) {
        this.engine = engine;
        return this;
    }

    public XmlLogiqua with(Map<String, Object> data) {
        if (engine != null) {
            throw new IllegalStateException("The engine is already set");
        }
        this.engine = Engine.withData(data);

        return this;
    }

    @Override
    public Script compile(String source) {
        final var xml = XmlReader.of(source).read();

        if (engine == null) {
            engine = Engine.withData(Map.of());
        }

        return XmlBuilder.from(xml, engine).build();
    }
}

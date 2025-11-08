package com.javax0.logiqua.yaml;

import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.scripts.ConstantValueNode;

import java.util.ArrayList;

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

    /*
plus:
     */

    private Script build(Object yamlObject) throws IllegalArgumentException {
return null;
    }
}

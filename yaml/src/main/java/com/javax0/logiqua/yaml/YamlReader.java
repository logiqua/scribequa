package com.javax0.logiqua.yaml;


import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlReader {

    private final String input;

    public static YamlReader of(final String input) {
        return new YamlReader(input);
    }

    private YamlReader(final String input) {
        this.input = input;
    }

    public Object read() {
        final var loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(50); // Prevent YAML bomb
        final var constructor = new SafeConstructor(loaderOptions) {
            @Override
            protected Object constructObject(Node node) {
                if (node instanceof ScalarNode scalarNode) {
                    if (scalarNode.getTag() == Tag.INT) {
                        return Long.valueOf(scalarNode.getValue());
                    } else if (node.getTag() == Tag.FLOAT) {
                        return Double.valueOf(scalarNode.getValue());
                    }
                }
                return super.constructObject(node);
            }
        };
        final var yaml = new Yaml(constructor);
        return yaml.load(input);
    }
}

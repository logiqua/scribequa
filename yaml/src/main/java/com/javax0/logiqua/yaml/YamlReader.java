package com.javax0.logiqua.yaml;


import org.yaml.snakeyaml.Yaml;

public class YamlReader {

    private final String input;

    public static YamlReader of(final String input) {
        return new YamlReader(input);
    }

    private YamlReader(final String input) {
        this.input = input;
    }

    public Object read() {
        final var yaml = new Yaml();
        return yaml.load(input);
    }

}

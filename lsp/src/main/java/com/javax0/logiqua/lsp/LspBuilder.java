package com.javax0.logiqua.lsp;

import com.javax0.lex.tokens.Identifier;
import com.javax0.lex.tokens.Symbol;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.engine.Engine;
import com.javax0.logiqua.scripts.ConstantValueNode;

import java.util.ArrayList;
import java.util.List;

public class LspBuilder {

    private final Object lspObject;
    private final Engine engine;

    public static LspBuilder from(Object lspObject, Engine engine) {
        return new LspBuilder(lspObject, engine);
    }

    private LspBuilder(Object lspObject, Engine engine) {
        this.lspObject = lspObject;
        this.engine = engine;
    }

    public Script build() {
        return build(lspObject);
    }

    private Script build(Object object) throws IllegalArgumentException {
        if (object instanceof List<?> list) {
            final var firstElement = list.getFirst();

            if (firstElement instanceof Identifier id) {
                final var key = id.value();
                if (engine.getOperation(key).isPresent()) {
                    final var nodeBuilder = engine.getOp(key);
                    final var args = new ArrayList<Script>(list.size() - 1);
                    for (int i = 1; i < list.size(); i++) {
                        args.add(build(list.get(i)));
                    }
                    return nodeBuilder.subscripts(args.toArray(Script[]::new));
                } else {
                    final var args = new ArrayList<Script>(list.size() - 1);
                    for (Object item : list) {
                        args.add(build(item));
                    }
                    return new ConstantValueNode<>(args);
                }
            }
            if (firstElement instanceof Symbol symbol) {
                final var key = symbol.value();
                final var nodeBuilder = engine.getOp(key);
                final var args = new ArrayList<Script>(list.size() - 1);
                for (int i = 1; i < list.size(); i++) {
                    args.add(build(list.get(i)));
                }
                return nodeBuilder.subscripts(args.toArray(Script[]::new));
            }
            final var args = new ArrayList<>(list.size() - 1);
            args.addAll(list);
            return new ConstantValueNode<>(args);
        } else {
            if (object instanceof Identifier id) {
                final var nodeBuilder = engine.getOp("var");
                final var args = new ArrayList<Script>(1);
                args.add(new ConstantValueNode<>(id.value()));
                return nodeBuilder.subscripts(args.toArray(Script[]::new));
            } else {
                return new ConstantValueNode<>(object);
            }
        }
    }
}

package com.javax0.logiqua.engine;

import com.javax0.logiqua.Operation;
import com.javax0.logiqua.Script;
import com.javax0.logiqua.scripts.CommandNode;
import com.javax0.logiqua.scripts.ConstantValueNode;
import com.javax0.logiqua.scripts.FunctionNode;

import java.util.Arrays;

public interface Builder {
    class FunctionNodeBuilder extends NodeBuilder {
        private final Operation.Function operation;

        public FunctionNodeBuilder(Engine engine, Operation.Function operation) {
            super(engine);
            this.operation = operation;
        }

        @Override
        public Script subscripts(Script... scripts) {
            final var node = new FunctionNode(engine, operation, scripts);
            operation.checkArguments(scripts);
            return node;
        }

        @Override
        public Script args(Object... argsObj) {
            final var args = toScript(argsObj);
            final var node = new FunctionNode(engine, operation, args);
            operation.checkArguments(args);
            return node;
        }
    }

    class CommandNodeBuilder extends NodeBuilder {
        private final Operation.Command operation;

        public CommandNodeBuilder(Engine engine, Operation.Command operation) {
            super(engine);
            this.operation = operation;
        }

        @Override
        public Script subscripts(Script... scripts) {
            final var node = new CommandNode(engine, operation, scripts);
            operation.checkArguments(scripts);
            return node;
        }

        @Override
        public Script args(Object... argsObj) {
            final var args = toScript(argsObj);
            return subscripts(args);
        }
    }

    abstract class NodeBuilder {
        protected NodeBuilder(Engine engine) {
            this.engine = engine;
        }

        public abstract Script subscripts(Script... scrips);

        public abstract Script args(Object... args);

        protected final Engine engine;

        protected Script[] toScript(Object[] argsObj) {
            return Arrays.stream(argsObj)
                    .map(obj -> {
                        if (obj instanceof Script sc) {
                            return sc;
                        } else {
                            return engine.constant(obj);
                        }
                    }).toArray(Script[]::new);
        }
    }

    NodeBuilder getOp(String symbol);

    ConstantValueNode<?> constant(Object value);

}

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

        public FunctionNodeBuilder(Engine engine,Operation.Function operation) {
            super(engine);
            this.operation = operation;
        }

        @Override
        Script args(Object... argsObj) {
            final var args = toScript(argsObj);
            final var node = new FunctionNode(operation, args);
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
        Script args(Object... argsObj) {
            final var args = toScript(argsObj);
            final var node = new CommandNode(operation, args);
            operation.checkArguments(args);
            return node;
        }
    }

    abstract class NodeBuilder {
        protected NodeBuilder(Engine engine) {
            this.engine = engine;
        }

        abstract Script args(Object... args);
        protected final Engine engine;
        protected Script[] toScript(Object[] argsObj) {
            return Arrays.stream(argsObj)
                    .map( obj -> {
                        if( obj instanceof Script sc){
                            return sc;
                        }else {
                            return engine.constant(obj);
                        }
                    }).toArray(Script[]::new);
        }
    }

    NodeBuilder getOp(String symbol);

    ConstantValueNode<?> constant(Object value);

}

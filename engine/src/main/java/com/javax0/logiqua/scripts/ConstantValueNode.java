package com.javax0.logiqua.scripts;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Script;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstantValueNode<T> implements Script {

    private final T value;

    public ConstantValueNode(T value) {
        this.value = value;
    }

    @Override
    public Object evaluate() {
        return value;
    }

    @Override
    public Object evaluateUsing(Executor executor) {
        return value;
    }

    @Override
    public String jsonify() {
        return switch (value ){
            case String s -> "\""+s.replace("\"","\\\"")+"\"";
            case Map<?,?> map -> "{"+
                    map.entrySet().stream().map(e -> "\""+e.getKey()+"\":\""+e.getValue()+"\"").collect(Collectors.joining(","))
                    +"}";
            case List<?> list -> "["+list.stream().map(Object::toString).collect(Collectors.joining(","))+"]";
            default -> value.toString();
        };
    }
}
package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;

import java.util.ArrayList;

@Named.Symbol("merge")
@Operation.Limited(min = 1)
public class Merge implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var outList = new ArrayList<>();
        for( final var arg : args ){
            final var accessor = executor.getContext().accessor(arg);
            if( !(accessor instanceof Context.Indexed array) ){
                outList.add(arg);
            }else{
                for( int i = 0 ; i < array.size(); i++){
                    outList.add(array.get(i).get());
                }
            }
        }
        return outList;
    }
}

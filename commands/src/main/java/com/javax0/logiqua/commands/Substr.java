package com.javax0.logiqua.commands;

import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Castor;

@Named.Symbol("substr")
@Operation.Arity(min = 2, max = 3)
public class Substr implements Operation.Function {
    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var cast = new Castor(executor);
        final var baseString = cast.toString(args[0]).orElseThrow(() -> new IllegalArgumentException("The first argument of the substr command must be a string."));
        var start = cast.toLong(args[1]).orElseThrow(() -> new IllegalArgumentException("The second argument of the substr command must be a number.")).intValue();
        if( start < 0 ){
            start = baseString.length() + start;
        }

        int end;
        if( args.length == 2 ){
            end = baseString.length() - start;
        }else{
            end = cast.toLong(args[2]).orElseThrow(() -> new IllegalArgumentException("The third argument of the substr command must be a number.")).intValue();
        }

        if( end < 0 ){
            end = baseString.length() + end;
        }else{
            end = start + end;
        }
        if( start >= end ){
            throw new IllegalArgumentException("The start index must be less than the end index.");
        }
        if( start < 0 || start >= baseString.length() ){
            throw new IllegalArgumentException("The start index must be in the range of the string.");
        }
        if( end > baseString.length() ){
            throw new IllegalArgumentException("The end index must be in the range of the string.");
        }

        return baseString.substring(start, end);
    }
}

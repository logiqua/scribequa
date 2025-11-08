package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Named;
import com.javax0.logiqua.Operation;
import com.javax0.logiqua.commands.utils.Castor;

import java.util.Objects;

@Named.Symbol("in")
@Operation.Limited(min = 2, max = 2)
public class In implements Operation.Function {

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var member = args[0];
        final var containedIn = args[1];
        final var accessor = executor.getContext().accessor(containedIn);
        if (accessor instanceof Context.Indexed inList) {
            for (int i = 0; i < inList.size(); i++) {
                final var item = inList.get(i).get();
                if (member == item || executor.getContext().caster(Context.classOf(item), Context.classOf(member))
                        .map(c -> c.cast(item)).map( c -> Objects.equals(c, member)).orElse(false)) {
                    return true;
                }
            }
            return false;
        } else {
            if( containedIn == member ){
                return true;
            }
            if( containedIn == null || member == null ){
                return false;
            }
            final var cast = new Castor(executor);
            final var containing = cast.toString(containedIn).orElseThrow(() -> new IllegalArgumentException("The second argument of the 'in' command must be a list or a string."));
            final var contained = cast.toString(member).orElseThrow(() -> new IllegalArgumentException("The second argument of the 'in' command must be a list or a string."));
            return containing.contains(contained );
        }
    }
}

package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.util.function.BiPredicate;

abstract class Between implements Operation.Function {
    abstract BiPredicate<Comparable<?>, Comparable<?>> comparator();

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var base = Context.classOf(args[0]);
        if( args[0] == null ){
            return false;
        }
        if (!(args[0] instanceof Comparable<?> prior)) {
            throw new IllegalArgumentException("The arguments of '" + symbol() + "' command must be comparable.");
        }
        for (int i = 1; i < args.length; i++) {
            final var current = (Comparable<?>) executor.getContext().caster(Context.classOf(args[i]), base)
                    .orElseThrow(() -> new IllegalArgumentException("The arguments of the equals command must be of the same type after coercion."))
                    .cast(args[i]);
            if (!comparator().test(prior, current)) {
                return false;
            }
            prior = current;
        }
        return true;
    }
}

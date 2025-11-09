package com.javax0.logiqua;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

/**
 * An operation is something that can operate on values, either evaluated or unevaluated.
 */
public sealed interface Operation extends Named permits Operation.Function, Operation.Macro {

    @Retention(RetentionPolicy.RUNTIME)
    @interface Arity {
        int min() default -1;

        int max() default -1;

        int[] NOT = new int[]{-1, -1};
    }

    /**
     * An operation may optionally implement this method to specify the limits of the arguments.
     * The returned array must have two elements, the first one is the minimum number of arguments,
     * the second one is the maximum number of arguments.
     * If the operation does not implement this method, then the default implementation returns
     * {@code [-1,-1]}, meaning that the operation can take any number of arguments, there is no required minimum
     * and no required maximum.
     * <p>
     * The simplest way to implement the limits is to annotate the operation class with the {@link Arity} annotation.
     *
     * @return the two-element array with the minimum and maximum number of arguments.
     */
    default int[] getArity() {
        final var ann = this.getClass().getAnnotation(Arity.class);
        if (ann == null) {
            return Arity.NOT;
        }
        return new int[]{ann.min(), ann.max()};
    }

    /**
     * Check the number and possibly the types of the arguments.
     * <p>
     * The default implementation checks that the number of arguments is between the limits specified by the min and
     * the max values.
     * <p>
     * Commands and functions are free to implement any other checks, that presumably may also check not only the
     * number of the arguments, but also the types of the arguments.
     * <p>
     * The implementation should throw an IllegalArgumentException if the number of arguments or something else it checked
     * is not valid.
     *
     * @param args the arguments passed to the operation. Note that these are unevaluated for commands and for functions
     *             as well. The method is not expected to evaluate them, especially since no executor is available.
     */
    default void checkArguments(Script... args) {
        final var limits = getArity();
        if (limits[0] != -1 && limits[0] > args.length) {
            throw new IllegalArgumentException("The operation " + this.getClass().getName() + " requires at least " + limits[0] + " arguments but "
                    + (args.length == 0 ? "none" : "only " + args.length) + " were provided.");
        }
        if (limits[1] != -1 && limits[1] < args.length) {
            throw new IllegalArgumentException("The operation " + this.getClass().getName() + " requires at most " + limits[1] + " arguments but "
                    + args.length + " were provided.");
        }
    }

    /**
     * A function is an operation that can be evaluated.
     * The evaluation starts AFTER the arguments are evaluated.
     */
    non-sealed interface Function extends Operation {
        Object evaluate(Executor executor, Object... args);
    }

    /**
     * A command is an operation that can be evaluated.
     * The evaluation starts BEFORE the arguments are evaluated.
     * The command can evaluate the arguments at it's discretion.
     */
    non-sealed interface Macro extends Operation {
        Object evaluate(Executor executor, Script... args);
    }
}

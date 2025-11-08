package com.javax0.logiqua;

import java.util.Set;

/**
 * A Script object represents the AST of the calculation.
 * <p>
 * A Script contains an operation and an array of arguments.
 * <p>
 * An operation can be something like '+' meaning it adds numbers together.
 * In that case, the arguments will give the numbers to sum.
 * Each argument is a Script object that can be evaluated, and then the results are passed to the operation.
 *
 */
public interface Script {

    /**
     * A script can evaluate itself.
     *
     * @return the result of the evaluation
     */
    Object evaluate();

    /**
     * A script can evaluate itself using the given executor.
     * The executor has to be "compatible" with the one that was used to compile the script.
     * <p>
     * This API is called to implement commands like map, aggregate etc. that provide a local context.
     *
     * @return the result of the evaluation
     */
    Object evaluateUsing(Executor executor);

    /**
     * Returns the set of classes representing the possible return types of this script when evaluated.
     * <p>
     * The typical implementation of this method will just call the {@link Operation#returns(Script...)} passing the
     * underlying scripts as an argument, when there are underlying scripts.
     *
     * @return a set of Class objects corresponding to the possible types that the evaluation might return
     */
    Set<Class<?>> returns();
}

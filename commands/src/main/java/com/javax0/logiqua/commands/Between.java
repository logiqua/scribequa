package com.javax0.logiqua.commands;

import com.javax0.logiqua.Context;
import com.javax0.logiqua.Executor;
import com.javax0.logiqua.Operation;

import java.util.function.BiPredicate;

/**
 * Abstract class representing a custom operation that evaluates a sequence of comparable objects
 * against a specific comparison logic. The comparison logic is determined by the implementing
 * class, using a {@link BiPredicate} provided by the {@link #comparator()} method.
 * <p>
 * Subclasses of this class are required to define the comparison logic by implementing the
 * {@link #comparator()} method. The comparison is applied pairwise to the arguments, starting
 * from the first argument, and evaluates to {@code true} or {@code false} depending on whether
 * the entire sequence conforms to the defined comparison logic.
 * <p>
 * This class implements the {@link Operation.Function} interface, allowing its implementation of
 * the {@link #evaluate(Executor, Object...)} method to validate and process the arguments, ensuring
 * they are comparable and applying the comparison logic.
 * <p>
 * The comparison logic is implemented comparing element following each other.
 * The operation
 *
 * <pre>
 *     (< 1 3 2) # lisp notation for the sake of example and using '>' as an example operator
 * </pre>
 * will compare {@code 1} with {@code 3}, and then {@code 3} with {@code 2}.
 * Hence, the above expression above evaluates to {@code false}.
 * <p>
 * When comparing the individual elements, the algorithm checks that they are all comparable.
 * During the individual comparisons, when the type of the compared elements are not the same one of them will be cast
 * to the type of the other.
 * <p>
 * If the later argument can be cast to the former, the casted value is used for the comparison.
 * Only if the later argument cannot be cast to the former, the prior value is cast and used for the comparison.
 *
 */
abstract class Between implements Operation.Function {
    abstract BiPredicate<Comparable<?>, Comparable<?>> comparator();

    @Override
    public Object evaluate(Executor executor, Object... args) {
        final var base = Context.classOf(args[0]);
        if (args[0] == null) {
            return false;
        }

        ensureAllArgumntsAreComparable(args);


        for (int i = 1; i < args.length; i++) {
            final var prior = (Comparable<?>) args[i - 1];
            final var current = (Comparable<?>) args[i];
            final var currentCaster = executor.getContext().caster(Context.classOf(current), Context.classOf(prior));
            final var priorCaster = executor.getContext().caster(Context.classOf(prior), Context.classOf(current));
            if (currentCaster.isEmpty() && priorCaster.isEmpty()) {
                throw new IllegalArgumentException("The arguments of '" + symbol() + "' command are not compatible to compare.\n" +
                        "Left hand side: " + prior.getClass().getCanonicalName() +
                        "\n Right hand side: " + current.getClass().getCanonicalName());
            }
            final var currentCasted = currentCaster.map(c -> c.cast(current)).map(Comparable.class::cast).orElse(current);
            final var priorCasted = currentCaster.isEmpty() ? priorCaster.map(c -> c.cast(prior)).map(Comparable.class::cast).orElse(prior) : prior;
            if (!comparator().test(priorCasted, currentCasted)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Ensures that all elements in the provided array are instances of {@link Comparable}.
     * If any element is not comparable, an {@link IllegalArgumentException} is thrown,
     * indicating the specific argument and its type.
     *
     * @param args the array of objects whose elements need to be checked
     *             for being instances of {@link Comparable}
     * @throws IllegalArgumentException if any element of the array is not comparable
     */
    private void ensureAllArgumntsAreComparable(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (!(args[i] instanceof Comparable<?>)) {
                new IllegalArgumentException("The arguments of '" + symbol() + "' command must be comparable.\n" +
                        "Argument " + i + " is " + args[i].getClass().getCanonicalName() + " not comparable.");
            }
        }
    }
}

package com.javax0.logiqua;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Something that is named should implement this interface.
 * The name is then returned by the method "symbol".
 * <p>
 * The default implementation uses annotation and reflection.
 * The classes that do not have an instance specific name can be annotated with the {@link Named.Symbol} annotation.
 */
public interface Named {
    @Target(java.lang.annotation.ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Symbol {
        String value();
    }

    /**
     * Returns the identifier name of this function used for registration in the global heap space.
     * This name is used to reference and call the function within the Turi language environment.
     * The name must be unique within the scope where the function is registered.
     * <p>
     * The default implementation uses annotation and reflection, which may seem a performance issue, but
     * this method is called when the methods are registered and not when used.
     *
     * @return the unique name of the function used for registration and reference in the global space
     */
    default String symbol() {
        final var ann = this.getClass().getAnnotation(Symbol.class);
        if (ann != null) {
            return ann.value();
        }
        return this.getClass().getSimpleName();
    }

}
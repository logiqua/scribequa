package com.javax0.logiqua;

import java.util.Optional;

/**
 * An execution context that holds parameters for an execution.
 * This is like a Map containing string keys and values that can also be maps, arrays, and other objects.
 */
public interface Context {

    /**
     * Represents a generalized collection-like structure that can act as a base for specific types of collection behaviors.
     * This interface is part of the execution context model, enabling flexible handling of map-like or list-like
     * collection patterns.
     * <p>
     * CollectionLike acts as a sealed interface which can only be directly implemented by {@code Mapped}
     * and {@code Indexed}, both of which extend specific collection handling behavior. Classes extending
     * {@code Mapped} or {@code Indexed} are expected to provide contextual value retrieval mechanisms.
     * <p>
     * This interface serves as a unification of map-like and list-like operations, providing a flexible foundation
     * for implementation and extension of collection-style behaviors in execution contexts.
     */
    sealed interface Proxy permits MappedProxy, IndexedProxy {
    }

    sealed interface Accessor permits Mapped, Indexed {

    }

    /**
     * A functional interface representing a transformer or converter that can transform an object
     * of type `From` into an object of type `To`.
     *
     * @param <From> the source type of the object to be transformed
     * @param <To>   the target type of the transformation
     */
    @FunctionalInterface
    public interface Caster<From, To> {
        To cast(From from);
    }

    /**
     * The MapLike interface represents a structure that mimics map-like behavior, allowing key-value access patterns.
     * It is a non-sealed interface that extends the Like interface, which provides a mechanism for deriving behavior
     * from a contextual value.
     * <p>
     * Implementations of this interface are responsible for providing a means to retrieve a contextual value associated
     * with a specific key from a given target object. This enables flexible handling of a collection-style object
     * behaviors in an execution context.
     */
    @FunctionalInterface
    non-sealed interface MappedProxy extends Proxy {
        Mapped get(Object target);
    }


    @FunctionalInterface
    non-sealed interface Mapped extends Accessor {
        Value get(String key);
    }

    /**
     * The `Indexed` interface represents a structure that mimics list-like behavior, allowing indexed access patterns.
     * It is a non-sealed interface that extends the `Like` interface, which provides a mechanism for deriving behavior
     * from a contextual value.
     * <p>
     * Implementations of this interface are responsible for providing a means to retrieve a contextual value
     * associated with a specific index from a given target object. This enables flexible handling of collection-style
     * object behaviors, particularly in an execution context.
     * <p>
     * The primary method, `get`, is designed to access a contextual value using an index-based approach,
     * similar to elements in a list. It takes a target object and an integer index, and returns the corresponding value
     * wrapped in a `Context.Value` object.
     */
    @FunctionalInterface
    non-sealed interface IndexedProxy extends Proxy {
        Indexed get(Object target);
    }

    /**
     * The Indexed interface represents a collection-like structure that allows
     * access to elements by their positional index.
     * <p>
     * This interface provides methods to retrieve the size of the collection
     * and to access elements at a specific index.
     * <p>
     * The interface is non-sealed, allowing additional implementation flexibility
     * and customization beyond the defined boundaries of the sealed Accessor interface.
     */
    non-sealed interface Indexed extends Accessor {
        Value get(int index);

        int size();
    }

    /**
     * A wrapper for a contextual value.
     * This is to distinguish between {@code null} values and values that are not present.
     * <p>
     * This wrapper is almost like a {@code java.util.Optional}, but it is not a generic type.
     * We use this wrapper instead of an optional, because when returning an optional from a method, it is assumed to be
     * not {@code null}. When the return type is {@code Value}, then it can also be {@code null}, which is means the
     * value is not present. With the optional, you cannot return a value that is present and {@ @code null}.
     *
     * @param get the actual value wrapped in this object. Also, the getter for this is called {@code get()}.
     */
    record Value(Object get) {
        public static Value of(Object get) {
            return new Value(get);
        }
    }

    /**
     * Get the value from the context associated with the given key.
     * If the key is not found, returns null.
     * <p>
     * It is up to the implementation to implement a single flat data structure or hierarchical keys.
     * The implementation {@code MapContext} implements hierarchical keys.
     *
     * @param key the key to look up
     * @return the value associated with the key embedded in a Value, or null if not found.
     */
    Value get(String key);

    /**
     * Returns a proxy that can be used to access the target object as a collection-like structure.
     * The returned proxy can be {@link MappedProxy} or {@link IndexedProxy}.
     *
     * @param target the object that we want to access as a collection-like structure. Note that this reference is also
     *               passed as a parameter to the proxy method {@link MappedProxy#get(Object)} or
     *               {@link IndexedProxy#get(Object)} as first parameter.
     *               It must be the same target.
     *               The accessor may keep the reference passed to this method and use this or the one passed as a
     *               parameter to the proxy method as it decides. Hence, the reference passed here and to the 'get'
     *               method MUST be the same.
     * @return either a {@link MappedProxy} or {@link IndexedProxy} proxy, or null if the target object is not a collection-like object
     * The structure and the actual context do not have any handler for the type of the target.
     */
    Accessor accessor(Object target);

    /**
     * Attempts to retrieve a caster that can transform an object of type {@code From} into an object of type {@code To}.
     *
     * @param <From> the source type to be transformed
     * @param <To>   the target type of the transformation
     * @param from   the {@code Class} object representing the source type
     * @param to     the {@code Class} object representing the target type
     * @return an {@code Optional} containing a {@code Caster<From, To>} if a suitable caster is available,
     * or an empty {@code Optional} if no such caster exists
     */
    <From, To> Optional<Caster<From, To>> caster(Class<From> from, Class<To> to);

    /**
     * Retrieves the runtime {@code Class} object of the specified target object.
     *
     * @param <Target> the type of the input target object
     * @param target   the object whose runtime {@code Class} is to be determined
     * @return the {@code Class} object representing the runtime class of the given target object
     */
    @SuppressWarnings("unchecked")
    static <Target> Class<Object> classOf(Target target) {
        if (target == null) {
            return null;
        } else {
            return (Class<Object>) target.getClass();
        }
    }
}

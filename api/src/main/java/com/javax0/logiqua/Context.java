package com.javax0.logiqua;

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
     * CollectionLike acts as a sealed interface which can only be directly implemented by {@code MapLike}
     * and {@code ListLike}, both of which extend specific collection handling behavior. Classes extending
     * {@code MapLike} or {@code ListLike} are expected to provide contextual value retrieval mechanisms.
     * <p>
     * This interface serves as a unification of map-like and list-like operations, providing a flexible foundation
     * for implementation and extension of collection-style behaviors in execution contexts.
     */
    sealed interface CollectionLike permits MapLike, ListLike {
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
    non-sealed interface MapLike extends CollectionLike {
        Context.Value get(Object target, String key);
    }

    /**
     * The `ListLike` interface represents a structure that mimics list-like behavior, allowing indexed access patterns.
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
    non-sealed interface ListLike extends CollectionLike {
        Context.Value get(Object target, int index);

        int size(Object target);
    }

    /**
     * A wrapper for a contextual value.
     * This is to distinguish between null values and values that are not present.
     *
     * @param get the actual value wrapped in this object. Also, the getter for this is called {@code get()}.
     */
    record Value(Object get) {
        public static Value of(Object get) {
            return new Value(get);
        }
    }

    /**
     * Get the value associated with the given key.
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
     * The returned  proxy can be {@link MapLike} or {@link ListLike}.
     *
     * @param target the object that we want to access as a collection-like structure. Note that this reference is also
     *               passed as a parameter to the proxy method {@link MapLike#get(Object, String)} or
     *               {@link ListLike#get(Object, int)} as first parameter.
     *               It must be the same target.
     *               The accessor may keep the reference passed to this method and use this or the one passed as a
     *               parameter to the proxy method as it decides. Hence, the reference passed here and to the 'get'
     *               method MUST be the same.
     * @return either a {@link MapLike} or {@link ListLike} proxy, or null if the target object is not a collection-like object
     * The structure and the actual context do not have any handler for the type of the target.
     */
    CollectionLike accessor(Object target);
}

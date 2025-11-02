package com.javax0.logiqua.engine;

import com.javax0.logiqua.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * The CasterRegistry is a utility class designed to manage and retrieve caster instances
 * for converting objects between two different types. This registry enables the dynamic
 * registration of custom casters and provides a mechanism to retrieve them based on the
 * source and target types.
 */
public class CasterRegistry {

    private final Map<Class<?>, ProxyRegistry<Context.Caster<?, ?>>> casters = new HashMap<>();

    /**
     * Registers a caster for converting objects from one type to another.
     * The caster is registered in an internal structure, associating the target class
     * with a registry of source-class-to-caster relationships.
     *
     * @param from   the source class representing the type of objects the caster will convert from
     * @param to     the target class representing the type of objects the caster will convert to
     * @param caster the caster implementation responsible for performing the conversion
     *               between the source and target types
     * @throws IllegalArgumentException if a caster for the `from` class is already
     *                                  registered in the internal registry for the specified `to` class
     */
    public void register(Class<?> from, Class<?> to, Context.Caster<?, ?> caster) {
        casters.computeIfAbsent(to, k -> new ProxyRegistry<>()).register(from, caster);
    }

    /**
     * Retrieves a caster that converts objects from the specified source type to the specified target type.
     * If no such caster is registered in the internal registry, this method returns null.
     *
     * @param from   the source class representing the type of objects the retrieved caster will convert from
     * @param to     the target class representing the type of objects the retrieved caster will convert to
     * @param <From> the generic parameter specifying the source type
     * @param <To>   the generic parameter specifying the target type
     * @return the caster instance capable of converting objects between the source and target types,
     * or null if no such caster is registered
     */
    @SuppressWarnings("unchecked")
    public <From, To> Context.Caster<From, To> get(Class<From> from, Class<To> to) {
        final var registry = casters.get(to);
        if (registry == null) {
            return null;
        }
        return (Context.Caster<From, To>) registry.getProxy(from);
    }

}

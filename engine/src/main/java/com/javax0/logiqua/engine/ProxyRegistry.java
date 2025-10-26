package com.javax0.logiqua.engine;

import java.util.ArrayList;

/**
 * A generic registry that manages proxies for specific interfaces or classes.
 * <p>
 * A proxy object implements certain functionality to perform specific tasks or operations on certain object types.
 * <p>
 * An example can be a proxy that fetches named data from some object. A Proxy registered on Map-s will get
 * the map instance and call a 'get()' on it. A Proxy registered on a JSONObject will get the JSONObject instance
 * and call their respective method.
 * <p>
 * This feature is used to allow implementation of MapLike and ArrayLike operations on objects that are not
 * maps or arrays. Instead of hard-coding the different types, proxies can be registered for the interfaces or the
 * classes, and the engine will use them automatically.
 * <p>
 * This gives the ability to the embedding application to add new types to the engine without having to modify the
 * engine code.
 *
 * @param <T> the type of proxy managed by the registry
 */
public class ProxyRegistry<T> {
    private class Proxy {
        private final Class<?> forInterface;
        private final T proxy;

        private Proxy(Class<?> forInterface, T proxy) {
            this.forInterface = forInterface;
            this.proxy = proxy;
        }
    }

    ArrayList<Proxy> proxies = new ArrayList<>();

    /**
     * Retrieves a proxy instance associated with the specified class.
     * The method searches for a proxy that is registered for a class or interface
     * that is assignable from the specified class. If no such proxy is found, it returns null.
     *
     * @param forClass the class or interface for which the proxy is requested
     * @return the proxy instance registered for the specified class, or null if no matching proxy is found
     */
    public T getProxy(Class<?> forClass) {
        return proxies.stream()
                .filter(p -> p.forInterface.isAssignableFrom(forClass))
                .findFirst()
                .map(p -> p.proxy)
                .orElse(null);
    }

    /**
     * Registers a proxy instance for a specified interface or class. If a proxy is already
     * registered for the given interface or class, this method throws an exception.
     * <p>
     * When registering proxies for interfaces that have inheritance relationships, the order of
     * registration matters. For example, if interface B extends interface A:
     * - Registering a proxy for A first, then B will work
     * - Registering a proxy for B first, then A will fail
     * This is because, when registering A after B, the system detects that B's proxy already handles A
     * (since B extends A), resulting in an IllegalArgumentException.
     * <p>
     * In other words, you should register first the more restrictive type and then the general one.
     *
     * @param forInterface the interface or class to register the proxy for
     * @param proxy        the proxy instance to be associated with the specified interface or class
     * @throws IllegalArgumentException if a proxy is already registered for the specified interface or class
     */
    public void register(Class<?> forInterface, T proxy) {
        final var existingForInterface = getProxy(forInterface);
        if (existingForInterface != null) {
            throw new IllegalArgumentException("The class '" + forInterface + "' is already registered as '" + existingForInterface + "'");
        }
        proxies.add(new Proxy(forInterface, proxy));
    }
}

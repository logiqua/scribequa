package com.javax0.logiqua.commands.utils;

import com.javax0.logiqua.Context;

import java.util.Map;
import java.util.Optional;

public class SimpleProxyMapContext implements Context {
    final Map<String, Object> map;
    final Context mapParent;
    final Context parent;

    /**
     * Create a new context that delegates to the specified parent context.
     * Use this constructor when you want to create a context that delegates to the parent context when fetching values.
     *
     * @param map    the map to delegate the value requests to if the value is not found locally
     * @param parent the map and another parent to delegate to
     */
    public SimpleProxyMapContext(Map<String, Object> map, Context parent) {
        this(map, parent, parent);
    }

    /**
     * Create a new context that delegates to the specified parent context.
     * Value requests will be delegated to the {@code mapParent} context if not found locally.
     * Other requests are delegated to the {@code parent} context.
     * <p>
     * Note that the {@code mapParent} can be null.
     * In all other cases, the two parents are the same.
     *
     * @param map       the map to delegate the value requests to if the value is not found locally
     * @param mapParent the parent to delegate the value requests to if the value is not found locally
     * @param parent    the parent to delegate all other requests to
     */
    public SimpleProxyMapContext(Map<String, Object> map, Context mapParent, Context parent) {
        this.map = map;
        this.mapParent = mapParent;
        this.parent = parent;
    }


    /**
     * Get the value of the key. If the key is not found in the map and then fetch it from the parent if there is a
     * parent.
     * <p>
     * Note that this method uses the mapParent delegate that may be null.
     *
     * @param key the key to look up
     * @return the value associated with the key embedded in a Value, or null if not found.
     */
    @Override
    public Value get(String key) {
        if (map.containsKey(key)) {
            return Value.of(map.get(key));
        } else if (mapParent != null) {
            return mapParent.get(key);
        } else
            return null;
    }

    /**
     * delegate to {@code parent} if not null
     *
     * @param target see the definition in the interface
     * @return see the definition in the interface
     */
    @Override
    public Accessor accessor(Object target) {
        return parent != null ? parent.accessor(target) : null;
    }

    /**
     * delegate to {@code parent} if not null
     *
     * @param from   see the definition in the interface
     * @param to     see the definition in the interface
     * @param <From> see the definition in the interface
     * @param <To>   see the definition in the interface
     * @return see the definition in the interface
     */
    @Override
    public <From, To> Optional<Caster<From, To>> caster(Class<From> from, Class<To> to) {
        return parent != null ? parent.caster(from, to) : Optional.empty();
    }
}

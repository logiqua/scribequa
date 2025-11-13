package com.javax0.logiqua.engine;

import com.javax0.logiqua.Context;

import java.util.Map;
import java.util.Optional;

public class HierarchicalMapContext extends MapContext {
    final Context mapParent;
    final Context parent;

    /**
     * Create a new context that delegates to the specified parent context.
     * Use this constructor when you want to create a context that delegates to the parent context when fetching values.
     *
     * @param map    the map to delegate the value requests to if the value is not found locally
     * @param parent the map and another parent to delegate to
     */
    public HierarchicalMapContext(Map<String, Object> map, Context parent) {
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
    public HierarchicalMapContext(Map<String, Object> map, Context mapParent, Context parent) {
        super(map);
        this.mapParent = mapParent;
        this.parent = parent;
    }


    /**
     * Get the value of the key. The approach is the following:
     *
     * <ul>
     *     <li>try to find the key in the local map
     *     <li>check if there is a key "" (empty string) defined in the current map, and if there is, then try to find the key in the value of the key ""
     *     <li>try to find the key in the parent context if there is a parent context
     * </ul>
     *
     * <p>
     * Note that this method uses the mapParent delegate that may be null.
     *
     * @param key the key to look up
     * @return the value associated with the key embedded in a Value, or {@code null} if not found.
     */
    @Override
    public Value get(String key) {
        final var value = super.get(key);
        if (value != null) {
            return value;
        }

        final var loopValue = super.get("");
        if (loopValue != null) {
            final var inLoop = get(key, loopValue.get());
            if (inLoop != null) {
                return inLoop;
            }
        }

        if (mapParent != null) {
            return get(key, mapParent);
        }

        return null;
    }

    /**
     * delegate to {@code parent} if not null
     *
     * @param target see the definition in the interface
     * @return see the definition in the interface
     */
    @Override
    public Proxy accessor(Object target) {
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

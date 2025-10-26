package com.javax0.logiqua.engine;

import com.javax0.logiqua.Context;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 * The MapContext class is an implementation of the Context interface that uses
 * a Map as the underlying data structure to store key-value pairs. It provides
 * methods to access and navigate through the stored values for a given key.
 * <p>
 * The keys can be structural, containing
 *
 * <pre>
 *     {@code key.subkey}
 * </pre>
 * <p>
 * style access to map elements and arrays as
 *
 * <pre>
 *     {@code key[index]}
 * </pre>
 * <p>
 * The implementation is liberal, {@code key.3} will return the value of the fourth element of the array and also
 * {@code key[subkey]} will return the value of the subkey.
 * <p>
 * The implementation handles anything
 * <ul>
 *     <li> anything that is an object of a class that implements the interface (ATIAOOACTITI) {@link Context}
 *     <li> ATIAOOACTITI {@link Map}
 *     <li> ATIAOOACTITI {@link List}
 *     <li> ATIAOOACTITI {@link Collection}
 *     <li> ATIAOOACTITI {@link Iterable}
 *     <li> anything that is a Java array {@code Object[]}
 *     <li>
 * </ul>
 * <p>
 * The MapContext class also maintains two proxies for {@link MapLike} and {@link ListLike} interfaces.
 * Any class implementing one of these interfaces with an instance registered with the MapContext will be used
 * to handle access to an object that is not meeting any of the above conditions (not a {@link Map}, not a {@link List},
 * etc.).
 *
 */
public class MapContext implements Context {

    private final ProxyRegistry<MapLike> mapLikeProxyRegistry = new ProxyRegistry<>();
    private final ProxyRegistry<ListLike> listLikeProxyRegistry = new ProxyRegistry<>();

    private final Map<String, Object> map;
    public final Convenience convenience = new Convenience();

    public MapContext(Map<String, Object> map) {
        this.map = map;
    }

    public void registerLike(Class<?> forInterface, CollectionLike proxy) {
        switch (proxy) {
            case MapLike map -> mapLikeProxyRegistry.register(forInterface, map);
            case ListLike list -> listLikeProxyRegistry.register(forInterface, list);
        }
    }


    @Override
    public Context.Value get(String key) {
        return get(key, map);
    }

    public Context.Value get(String key, Object from) {
        final var dix = key.indexOf(".");
        final var pix = key.indexOf("[");
        if (dix != -1 || pix != -1) {
            final var parts = key.trim().split("\\s*[\\[.]\\s*", -1);
            Object iterator = from;
            for (final var part : parts) {
                String k = part.endsWith("]") ? part.substring(0, part.length() - 1) : part;
                final var iteratorV = get(k, iterator);
                if (iteratorV == null) {
                    return null;
                }
                iterator = iteratorV.get();
            }
            return Context.Value.of(iterator);
        } else {
            return switch (from) {
                case null -> {
                    if (key.isEmpty()) {
                        // if key is empty, we return the object, even if it is null
                        yield Context.Value.of(null);
                    } else {
                        // we will not find any non-empty key indexed whatnot in a null object
                        yield null;
                    }
                }
                case Context context -> Context.Value.of(context.get(key));
                case Map<?, ?> m -> m.containsKey(key) ? Context.Value.of(m.get(key)) : null;
                case List<?> list -> {
                    int index = parse(key);
                    if (index < 0) {
                        throw new NumberFormatException("Index must be a number");
                    }
                    if (index >= list.size()) {
                        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds > " + list.size());
                    }
                    yield Context.Value.of(list.get(index));
                }
                case Collection<?> collection -> get(key, List.of(collection));
                case Iterable<?> iterable -> get(key, List.of(iterable));
                case Object[] data -> get(key, Arrays.asList(data));
                default -> {
                    final var mapLike = mapLikeProxyRegistry.getProxy(from.getClass());
                    if (mapLike != null) {
                        yield mapLike.get(from, key);
                    } else {
                        final var listLike = listLikeProxyRegistry.getProxy(from.getClass());
                        if (listLike != null) {
                            yield listLike.get(from, parse(key));
                        }
                        throw new IllegalArgumentException("Cannot get the value from the Java Object '" + from.getClass().getName() + "' with key '" + key + "'");
                    }
                }
            };
        }
    }

    /**
     * Retrieves a {@link CollectionLike} proxy for the specified target object. The method first attempts to retrieve
     * a map-like proxy; if unavailable, it falls back to retrieving a list-like proxy.
     *
     * @param target the object for which the {@link CollectionLike} proxy is to be retrieved
     * @return a {@link CollectionLike} proxy for the specified target, or null if neither map-like nor list-like proxy is found
     */
    @Override
    public CollectionLike accessor(Object target) {
        if (target == null) {
            return (MapLike) (_, key) -> {
                if (key.isEmpty()) {
                    // if key is empty, we return the object, even if it is null
                    return Context.Value.of(null);
                } else {
                    // we will not find any non-empty key indexed whatnot in a null object
                    return null;
                }
            };
        }
        if( target.getClass().isArray()){
            return new ListLike() {

                @Override
                public int size(Object target) {
                    return ((Object[])target).length;
                }

                @Override
                public Value get(Object t, int index) {
                    if (index < 0) {
                        throw new NumberFormatException("Index must be a positive number");
                    }
                    if (index >= ((Object[])target).length) {
                        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds > " + ((Object[])target).length);
                    }
                    return Context.Value.of(((Object[])target)[index]);
                }
            };
        }
        return switch (target) {

            case Context context -> (MapLike) (_, k) -> Context.Value.of(context.get(k));
            case Map<?, ?> m -> (MapLike) (_, k) -> m.containsKey(k) ? Context.Value.of(m.get(k)) : null;
            case List<?> list -> new ListLike() {
                @Override
                public int size(Object target) {
                    return list.size();
                }

                @Override
                public Value get(Object t, int index) {
                    if (index < 0) {
                        throw new NumberFormatException("Index must be a positive number");
                    }
                    if (index >= list.size()) {
                        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds > " + list.size());
                    }
                    return Context.Value.of(list.get(index));
                }

            };
            case Collection<?> collection -> accessor(new ArrayList<>(collection));
            case Iterable<?> iterable -> accessor(StreamSupport.stream(iterable.spliterator(),false).toList());
            default -> {
                final var mapLike = mapLikeProxyRegistry.getProxy(target.getClass());
                if (mapLike != null) {
                    yield mapLike;
                } else {
                    final var listLike = listLikeProxyRegistry.getProxy(target.getClass());
                    if (listLike != null) {
                        yield listLike;
                    }
                    throw new IllegalArgumentException("Cannot use Java Object '" + target.getClass().getName() + "' as a List or MapLike compliant object");
                }
            }
        };
    }

    public class Convenience {
        private static class ReflectionMapLike implements MapLike {
            @Override
            public Context.Value get(Object target, String key) {
                final var field = Arrays.stream(target.getClass().getFields())
                        .filter(f -> f.getName().equals(key))
                        .findFirst();
                if (field.isEmpty()) {
                    return null;
                }
                return Context.Value.of(field.map(f -> {
                    try {
                        return f.get(target);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Must not happen");
                    }
                }).orElseThrow(() -> new IllegalArgumentException("Cannot get the value of the Java Object field '"
                        + key + "' from the Java Object '" + target.getClass().getName() + "'")));
            }
        }

        public void doJavaIntrospection() {
            registerLike(Object.class, new Convenience.ReflectionMapLike());
        }
    }


    private static int parse(String key) {
        if (key.isEmpty()) {
            return -1;
        }
        int value = 0;
        for (char c : key.toCharArray()) {
            if (Character.isDigit(c)) {
                value = value * 10 + Character.getNumericValue(c);
            } else {
                return -1;
            }
        }
        return value;
    }

}

package com.javax0.logiqua.engine;

import com.javax0.logiqua.Context;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
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
 * The MapContext class also maintains two proxies for {@link MappedProxyFactory} and {@link IndexedProxyFactory} interfaces.
 * Any class implementing one of these interfaces with an instance registered with the MapContext will be used
 * to handle access to an object that is not meeting any of the above conditions (not a {@link Map}, not a {@link List},
 * etc.).
 *
 */
public class MapContext implements Context {

    private final ProxyRegistry<MappedProxyFactory> mappedProxyRegistry = new ProxyRegistry<>();
    private final ProxyRegistry<IndexedProxyFactory> indexedProxyRegistry = new ProxyRegistry<>();
    private final CasterRegistry casters = new CasterRegistry();

    protected final Map<String, Object> map;
    public final Convenience convenience = new Convenience();

    public MapContext(Map<String, Object> map) {
        this.map = map;
        registerCaster(Integer.class, Long.class, Integer::longValue);
        registerCaster(int.class, Long.class, Integer::longValue);
        registerCaster(Short.class, Long.class, Short::longValue);
        registerCaster(short.class, Long.class, Short::longValue);
        registerCaster(Byte.class, Long.class, b -> {
            final var iby = (long) b;
            return iby < 0 ? iby + 256 : iby;
        });
        registerCaster(byte.class, Long.class, b -> {
            final var iby = (long) b;
            return iby < 0 ? iby + 256 : iby;
        });
        registerCaster(BigInteger.class, Long.class, BigInteger::longValue);

        registerCaster(Long.class, Integer.class, Long::intValue);
        registerCaster(long.class, Integer.class, Long::intValue);
        registerCaster(Short.class, Integer.class, Short::intValue);
        registerCaster(short.class, Integer.class, Short::intValue);
        registerCaster(Byte.class, Integer.class, b -> {
            final var iby = (int) b;
            return iby < 0 ? iby + 256 : iby;
        });
        registerCaster(byte.class, Integer.class, b -> {
            final var iby = (int) b;
            return iby < 0 ? iby + 256 : iby;
        });
        registerCaster(BigInteger.class, Integer.class, BigInteger::intValue);

        registerCaster(Integer.class, Short.class, Integer::shortValue);
        registerCaster(int.class, Short.class, Integer::shortValue);
        registerCaster(Long.class, Short.class, Long::shortValue);
        registerCaster(long.class, Short.class, Long::shortValue);
        registerCaster(Byte.class, Short.class, b -> {
            final var iby = (short) b;
            return (short) (iby < 0 ? iby + 256 : iby);
        });
        registerCaster(byte.class, Short.class, b -> {
            final var iby = (short) b;
            return (short) (iby < 0 ? iby + 256 : iby);
        });
        registerCaster(BigInteger.class, Short.class, BigInteger::shortValue);

        registerCaster(Long.class, Byte.class, Long::byteValue);
        registerCaster(long.class, Byte.class, Long::byteValue);
        registerCaster(Integer.class, Byte.class, Integer::byteValue);
        registerCaster(int.class, Byte.class, Integer::byteValue);
        registerCaster(Short.class, Byte.class, Short::byteValue);
        registerCaster(short.class, Byte.class, Short::byteValue);
        registerCaster(BigInteger.class, Byte.class, BigInteger::byteValue);

        registerCaster(Byte.class, Double.class, i -> (double) i);
        registerCaster(Short.class, Double.class, i -> (double) i);
        registerCaster(Integer.class, Double.class, i -> (double) i);
        registerCaster(Long.class, Double.class, i -> (double) i);
        registerCaster(Byte.class, Float.class, i -> (float) i);
        registerCaster(Short.class, Float.class, i -> (float) i);
        registerCaster(Integer.class, Float.class, i -> (float) i);
        registerCaster(Long.class, Float.class, i -> (float) i);

        registerCaster(Float.class, Double.class, Float::doubleValue);
        registerCaster(Double.class, Float.class, Double::floatValue);
        registerCaster(BigDecimal.class, Double.class, BigDecimal::doubleValue);

    }

    public <From, To> void registerCaster(Class<From> from, Class<To> to, Context.Caster<From, To> caster) {
        casters.register(from, to, caster);
    }

    public void registerProxy(Class<?> forInterface, ProxyFactory proxy) {
        switch (proxy) {
            case MappedProxyFactory m -> mappedProxyRegistry.register(forInterface, m);
            case IndexedProxyFactory l -> indexedProxyRegistry.register(forInterface, l);
        }
    }

    @Override
    public Context.Value get(String key) {
        if (key.isEmpty() && !map.containsKey("")) {
            return Context.Value.of(map);
        }
        return get(key, map);
    }

    /**
     * Retrieves a {@code Context.Value} instance corresponding to the given key from the specified object.
     * The method supports nested keys, indexed elements in collections, and mappings through a variety of
     * object structures, including maps, lists, arrays, and custom proxies.
     *
     * @param key  the key or index used to retrieve the value; can include nested keys separated by dots (e.g., "key.subkey")
     *             or indexes (e.g., "list[0]"). Whitespace around separators is ignored.
     * @param from the source object from which the value will be retrieved; it can be a {@code Map}, {@code List},
     *             {@code Collection}, {@code Object[]}, {@code Context}, or an object with mapped or indexed proxies.
     * @return a {@code Context.Value} instance representing the retrieved value if found;
     * returns {@code null} if the key is not found or if the source object is {@code null}
     * and the key is non-empty.
     * @throws IllegalArgumentException  if the source object type is unsupported for key access.
     * @throws NumberFormatException     if the key is expected to represent an index but is not a valid number.
     * @throws IndexOutOfBoundsException if the key references an index that is out of bounds in a list or array.
     */
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
                    final var mapLike = mappedProxyRegistry.getProxy(from.getClass());
                    if (mapLike != null) {
                        yield mapLike.get(from).get(key);
                    } else {
                        final var indexed = indexedProxyRegistry.getProxy(from.getClass());
                        if (indexed != null) {
                            yield indexed.get(from).get(parse(key));
                        }
                        throw new IllegalArgumentException("Cannot get the value from the Java Object '" + from.getClass().getName() + "' with key '" + key + "'");
                    }
                }
            };
        }
    }


    /**
     * Retrieves a caster capable of converting objects from the specified source type to the specified target type.
     * The method checks for primitive-to-wrapper conversions, exact type matches, or a registered caster
     * in the internal registry. If the target type is {@code String} and no caster is found, a default implementation
     * casting the object to a string is returned..
     *
     * @param fromClass the {@code Class} representing the source type of the objects to be converted
     * @param toClass   the {@code Class} representing the target type of the conversion
     * @param <From>    the generic type representing the source type
     * @param <To>      the generic type representing the target type
     * @return a {@code Caster} instance capable of converting objects of type {@code From} to type {@code To},
     * or {@code null} if no appropriate caster is found and the target type is not {@code String}
     */
    @SuppressWarnings("unchecked")
    public <From, To> Optional<Caster<From, To>> caster(Class<From> fromClass, Class<To> toClass) {
        if (toClass == fromClass || (fromClass.isPrimitive() && toClass == getWrapperClass(fromClass))) {
            return Optional.of(from -> (To) from);
        }
        final var caster = casters.get(fromClass, toClass);
        if (caster == null && toClass == String.class) {
            return Optional.of(from -> (To) Objects.toString(from));
        }
        return Optional.ofNullable(caster);
    }

    /**
     * Converts a primitive type {@code Class} to its corresponding wrapper type {@code Class}.
     * This method identifies the wrapper classes for all Java primitive types.
     *
     * @param primitive the {@code Class} object representing a primitive type
     * @return the {@code Class} object representing the wrapper type of the given primitive type
     * @throws IllegalArgumentException if the provided class is not a primitive type
     */
    private static Class<?> getWrapperClass(Class<?> primitive) {
        if (primitive == int.class) return Integer.class;
        if (primitive == long.class) return Long.class;
        if (primitive == double.class) return Double.class;
        if (primitive == float.class) return Float.class;
        if (primitive == boolean.class) return Boolean.class;
        if (primitive == byte.class) return Byte.class;
        if (primitive == short.class) return Short.class;
        if (primitive == char.class) return Character.class;
        throw new IllegalArgumentException("Unknown primitive: " + primitive);
    }

    /**
     * Retrieves a {@link ProxyFactory} proxy for the specified target object. The method first attempts to retrieve
     * a map-like proxy; if unavailable, it falls back to retrieving a list-like proxy.
     *
     * @param target the object for which the {@link ProxyFactory} proxy is to be retrieved
     * @return a {@link ProxyFactory} proxy for the specified target, or null if neither map-like nor list-like proxy is found
     */
    @Override
    public Proxy accessor(Object target) {
        if (target == null) {
            return (MappedProxy) (String key) -> {
                if (key.isEmpty()) {
                    // if key is empty, we return the object, even if it is null
                    return Context.Value.of(null);
                } else {
                    // we will not find any non-empty key indexed whatnot in a null object
                    return null;
                }
            };
        }
        if (target.getClass().isArray()) {
            return new IndexedProxy() {

                @Override
                public int size() {
                    return Array.getLength(target);
                }

                @Override
                public Value get(int index) {
                    if (index < 0) {
                        throw new NumberFormatException("Index must be a positive number");
                    }
                    if (index >= Array.getLength(target)) {
                        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds > " + ((Object[]) target).length);
                    }
                    return Context.Value.of(Array.get(target, index));
                }
            };
        }
        return switch (target) {

            case Context context -> (MappedProxy) (k) -> Context.Value.of(context.get(k));
            case Map<?, ?> m -> (MappedProxy) (k) -> m.containsKey(k) ? Context.Value.of(m.get(k)) : null;
            case List<?> list -> new IndexedProxy() {
                @Override
                public int size() {
                    return list.size();
                }

                @Override
                public Value get(int index) {
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
            case Iterable<?> iterable -> accessor(StreamSupport.stream(iterable.spliterator(), false).toList());
            default -> {
                final var mapLike = mappedProxyRegistry.getProxy(target.getClass());
                if (mapLike != null) {
                    yield mapLike.get(target);
                }
                final var indexed = indexedProxyRegistry.getProxy(target.getClass());
                if (indexed != null) {
                    yield indexed.get(target);
                }
                yield null;
            }

        };
    }

    public class Convenience {
        private static class ReflectionMappedProxyFactory implements MappedProxyFactory {
            @Override
            public MappedProxy get(Object target) {
                return key -> {
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
                };
            }
        }

        public void doJavaIntrospection() {
            registerProxy(Object.class, new ReflectionMappedProxyFactory());
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

    public Context sprout(Map<String, Object> data) {
        return new HierarchicalMapContext(data, this);
    }

}

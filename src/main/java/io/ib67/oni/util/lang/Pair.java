package io.ib67.oni.util.lang;

/**
 * Bundle object.
 * Also see: {@link Quadruple} {@link Triple}
 *
 * @since 1.0
 */
public class Pair<K, V> {
    public K key;
    public V value;

    protected Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public static <K, V> Pair<K, V> of(K a, V b) {
        return new Pair<>(a, b);
    }
}

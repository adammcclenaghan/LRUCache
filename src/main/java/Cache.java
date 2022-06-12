import java.util.Optional;

/**
 * A key value store
 * @param <K> The type for any key stored in this cache
 * @param <V> The type for any value stored in this cache
 */
public interface Cache <K, V> {

    /**
     * Set a key-value pair in the cache.
     * @param key   The key for the associated value
     * @param value The value
     */
    void set(final K key, final V value);

    /**
     * Get the value associated with the provided key from the cache
     * @param key The key associated with the value for retrieval
     * @return    An {@link Optional} encapsulating the value associated
     *            with this key.
     */
    Optional<V> get(final K key);
}

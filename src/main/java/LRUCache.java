import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An LRU cache implementation.
 *
 * @param <K> Key type stored in the cache
 * @param <V> Value type stored in the cache
 */
public class LRUCache <K,V> implements Cache <K,V> {

    public LRUCache(final int capacity) {
        if (capacity < 1)
            throw new IllegalArgumentException("Capacity cannot be less than 1");

        this.maxCapacity = capacity;
        map = new HashMap<>(capacity);
        queue = new RecencyQueue<>();
    }

    /**
     * Used to store new data in the cache.
     *
     * If the key already exists in the cache, its value will be updated.
     *
     * If the key does not exist in the cache, its value will be added, in
     * this case, if adding to the cache would cause it to grow beyond its
     * max capacity, the least recently used cache item will be evicted from
     * the cache before adding the new key-value pair.
     * @param key    The key for the corresponding value
     * @param value  The value to add to the cache
     */
    public void set(final K key, final V value) {

        CacheItem<K, V> cacheItem = new CacheItem<>(key, value);

        if (map.containsKey(key)) {
            // Cache hit, we should update the value of the key and
            // move it to the front of the queue
            RecencyQueue.Node<CacheItem<K, V>> node = map.get(key);
            // Updates the value and sets node as MRU
            queue.updateNode(node, cacheItem);
        }
        else {
            if (queue.getSize() == maxCapacity) {
                // Remove the LRU from the queue and the map
                RecencyQueue.Node<CacheItem<K, V>> last = queue.evictLRU();
                map.remove(last.getNodeItem().getKey());

                // Add the new item to the queue and the map
                RecencyQueue.Node<CacheItem<K, V>> newNode = queue.add(cacheItem);
                map.put(key, newNode);
            } else {
                RecencyQueue.Node<CacheItem<K, V>> newNode = newNode = queue.add(cacheItem);
                map.put(key, newNode);
            }
        }

    }

    /**
     * Retrieve the value from the cache which corresponds to the
     * provided key.
     * @param key Key for the value to retrieve
     * @return    An optional encapsulating the value.
     */
    public Optional<V> get(final K key) {
        if (map.containsKey(key)) {
            // Move it to the front of the queue
            RecencyQueue.Node<CacheItem<K, V>> node = map.get(key);
            queue.setMRU(node);
            return Optional.of(node.getNodeItem().getValue());
        }
        else {
            return Optional.empty();
        }
    }

    private final int maxCapacity;
    private final Map<K, RecencyQueue.Node<CacheItem<K, V>>> map;
    private final RecencyQueue<CacheItem<K, V>> queue;
}

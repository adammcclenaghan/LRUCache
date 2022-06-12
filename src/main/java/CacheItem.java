/**
 * This class is used to represent an item added to the {@link LRUCache}
 *
 * It is necessary to store the key here, because when an item is evicted
 * from the cache, we will have to remove it from both the RecencyQueue and
 * the Map held by the cache.
 *
 * To remove it from the RecencyQueue we can simply remove the last item in
 * the queue. However, to remove it efficiently from the map, we need to know
 * what key is associated with the evicted node. So by storing the key here, we avoid
 * having to iterate through the map to find the RecencyQueue Node with a value which matches
 * this evicted cache item.
 *
 * @param <K> The type of key stored in this CacheItem
 * @param <V> The type of value stored in this CacheItem
 */
public class CacheItem <K, V> {

    public CacheItem(K key, V value) {
        this.value = value;
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public String toString() {
        return "Key: %s Value: %s".formatted(key, value);
    }

    private K key;
    private V value;
}

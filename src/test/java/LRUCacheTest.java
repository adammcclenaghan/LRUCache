import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void shouldThrowIfCacheCapacityLessOne() {
        IllegalArgumentException expected = assertThrows(
                IllegalArgumentException.class,
                () -> new LRUCache<String, String>(0),
                "Should throw IllegalArgumentException when capacity is less than 1"
        );

        assertEquals("Capacity cannot be less than 1", expected.getMessage());
    }

    @Test
    void shouldReturnEmptyOptionalIfNoEntryInCache() {
        final Cache<Integer, String> cache = new LRUCache<>(1);
        Optional<String> value = cache.get(1);
        assertFalse(value.isPresent());
        assertEquals(Optional.empty(), value, "Optional should be empty when there is no cache entry");
    }

    // Should get item in cache
    @Test
    void shouldRetrieveItemFromCache() {
        final int key = 1;
        final String value = "Value one";

        final int keyTwo = 2;
        final String valueTwo = "Value two";

        final Cache<Integer, String> cache = new LRUCache<>(2);

        cache.set(key, value);
        cache.set(keyTwo, valueTwo);

        assertEquals(value, cache.get(key).get(), "Cache should contain value for key 1");
        assertEquals(valueTwo, cache.get(keyTwo).get(), "Cache should contain value for key 2");
    }

    @Test
    void shouldUpdateExistingValueIfKeyExists() {
        final int key = 1;
        final String value = "Value";
        final String newValue = "New Value";

        final Cache<Integer, String> cache = new LRUCache<>(1);
        cache.set(1, value);
        assertEquals(value, cache.get(key).get(), "Cache should contain value for key 1");

        cache.set(1, newValue);
        assertEquals(newValue, cache.get(key).get(), "Cache should contain updated value for key 1");
    }

    @Test
    void shouldEvictLruFromCache() {
        final int key = 1;
        final String value = "Value one";

        final int keyTwo = 2;
        final String valueTwo = "Value two";

        final Cache<Integer, String> cache = new LRUCache<>(1);

        cache.set(key, value);
        cache.set(keyTwo, valueTwo);

        assertFalse(cache.get(key).isPresent(), "Cache should have evicted value for key 1");
        assertEquals(valueTwo, cache.get(keyTwo).get(), "Cache should contain value for key 2");
    }

    @Test
    void shouldUpdateRecencyWithGet() {
        final int key = 1;
        final String value = "Value one";

        final int keyTwo = 2;
        final String valueTwo = "Value two";

        final int keyThree = 3;
        final String valueThree = "Value three";

        final Cache<Integer, String> cache = new LRUCache<>(2);
        cache.set(key, value);
        cache.set(keyTwo, valueTwo);

        /*
        Getting the value associated with "key" should set it to
        more recently used than keyTwo. So when we add another
        item to the cache, keyTwo should be evicted instead of key
         */
        cache.get(key);
        cache.set(keyThree, valueThree);

        assertEquals(value, cache.get(key).get(), "Cache should contain value for key 1 because cache.get" +
                " call should have set it to most recently used");
        assertFalse(cache.get(keyTwo).isPresent(), "Cache should not contain entry for key two as it should " +
                "be the least recently used");
        assertEquals(valueThree, cache.get(keyThree).get(), "Cache should contain entry for key three as it " +
                "is the second most recently used");
    }

    @Test
    void shouldUpdateRecencyWithUpdate() {
        final int key = 1;
        final String value = "Value one";
        final String altValue = "Value alt";

        final int keyTwo = 2;
        final String valueTwo = "Value two";

        final int keyThree = 3;
        final String valueThree = "Value three";

        final Cache<Integer, String> cache = new LRUCache<>(2);
        cache.set(key, value);
        cache.set(keyTwo, valueTwo);

        /*
        Getting the value associated with "key" should set it to
        more recently used than keyTwo. So when we add another
        item to the cache, keyTwo should be evicted instead of key
         */
        cache.set(key, altValue);
        cache.set(keyThree, valueThree);

        assertEquals(altValue, cache.get(key).get(), "Cache should contain entry for key 1 as updating its" +
                " value should have set it as the most recently used");
        assertFalse(cache.get(keyTwo).isPresent(), "Cache should not contain entry for key two as it should" +
                " be the least recently used");
        assertEquals(valueThree, cache.get(keyThree).get(), "Cache should contain entry for key three as it" +
                " is the second most recently used");
    }
}
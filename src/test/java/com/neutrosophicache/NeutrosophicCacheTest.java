package com.neutrosophicache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NeutrosophicCacheTest {

    private NeutrosophicCache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new NeutrosophicCache<>(3);
    }

    @Test
    void testPutAndGet() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    void testGetReturnsNullOnMiss() {
        assertNull(cache.get("nonexistent"));
    }

    @Test
    void testHitRateAfterHit() {
        cache.put("key1", "value1");
        cache.get("key1");
        assertEquals(100.0, cache.getHitRate(), 0.1);
    }

    @Test
    void testMissRateAfterMiss() {
        cache.get("missing");
        assertEquals(100.0, cache.getMissRate(), 0.1);
    }

    @Test
    void testHitAndMissRate() {
        cache.put("key1", "value1");
        cache.get("key1");
        cache.get("key2");
        assertEquals(50.0, cache.getHitRate(), 0.1);
        assertEquals(50.0, cache.getMissRate(), 0.1);
    }

    @Test
    void testCacheDoesNotExceedCapacity() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");
        assertEquals(3, cache.size());
    }

    @Test
    void testEvictionHappensWhenFull() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");
        assertTrue(cache.size() <= 3);
    }


    @Test
    void testPutSameKeyUpdatesAccess() {
        cache.put("key1", "value1");
        cache.put("key1", "value1");
        assertEquals(1, cache.size());
    }

    @Test
    void testContainsKeyReturnsTrueWhenPresent() {
        cache.put("key1", "value1");
        assertTrue(cache.containsKey("key1"));
    }

    @Test
    void testContainsKeyReturnsFalseWhenAbsent() {
        assertFalse(cache.containsKey("ghost"));
    }

    @Test
    void testResetClearsCache() {
        cache.put("key1", "value1");
        cache.reset();
        assertEquals(0, cache.size());
        assertNull(cache.get("key1"));
    }

    @Test
    void testResetClearsStats() {
        cache.put("key1", "value1");
        cache.get("key1");
        cache.reset();
        assertEquals(0.0, cache.getHitRate(), 0.1);
    }

    @Test
    void testIntegerKeyStringValue() {
        NeutrosophicCache<Integer, String> intCache =
                new NeutrosophicCache<>(5);
        intCache.put(1, "one");
        assertEquals("one", intCache.get(1));
    }

    @Test
    void testStringKeyObjectValue() {
        NeutrosophicCache<String, Integer> objCache =
                new NeutrosophicCache<>(5);
        objCache.put("score", 100);
        assertEquals(100, objCache.get("score"));
    }

    @Test
    void testCustomFalsityThreshold() {
        NeutrosophicCache<String, String> customCache =
                new NeutrosophicCache<>(3, 0.5);
        customCache.put("key1", "value1");
        assertEquals("value1", customCache.get("key1"));
    }

    @Test
    void testSingleCapacityCache() {
        NeutrosophicCache<String, String> tiny =
                new NeutrosophicCache<>(1);
        tiny.put("key1", "value1");
        tiny.put("key2", "value2");
        assertEquals(1, tiny.size());
    }

    @Test
    void testGetOnEmptyCache() {
        assertNull(cache.get("anything"));
    }

    @Test
    void testHitRateOnEmptyCache() {
        assertEquals(0.0, cache.getHitRate(), 0.1);
    }

    @Test
    void testZeroCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new NeutrosophicCache<>(0));
    }

    @Test
    void testNegativeCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new NeutrosophicCache<>(-1));
    }

    @Test
    void testInvalidFalsityThresholdThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new NeutrosophicCache<>(10, 1.5));
    }

    @Test
    void testNullKeyGetThrowsException() {
        assertThrows(NullPointerException.class, () ->
                cache.get(null));
    }

    @Test
    void testNullKeyPutThrowsException() {
        assertThrows(NullPointerException.class, () ->
                cache.put(null, "value"));
    }

    @Test
    void testNullValuePutThrowsException() {
        assertThrows(NullPointerException.class, () ->
                cache.put("key", null));
    }
}
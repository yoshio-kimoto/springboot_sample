package com.imanage.interview.cache;

import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K, V> implements Cache<K, V> {
    private Map<K, V> cache = new HashMap<>();

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        consumeCPU(5);
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    // Method to create variable CPU consumption instead of explicit delay
    private void consumeCPU(int intensity) {
        // Create a relatively expensive operation that forces context switching
        for (int i = 0; i < intensity * 1000; i++) {
            double result = Math.pow(Math.random() * 10, Math.random() * 5);
            result = Math.sqrt(result);
            // Force thread to yield to increase chances of race condition
            if (i % 100 == 0) {
                Thread.yield();
            }
        }
    }
}

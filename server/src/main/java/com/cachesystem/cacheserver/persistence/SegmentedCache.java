package com.cachesystem.cacheserver.persistence;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class SegmentedCache<K,V> {
    private int numberOfSegments;
    private final Segment<K,V>[] segments;
    @SuppressWarnings("unchecked")
    public SegmentedCache(int capacity, int numSegments) {
        this.numberOfSegments = numSegments;
        this.segments = new Segment[numSegments];
        int segmentCapacity = Math.max(1, capacity / numSegments);

        for (int i = 0; i < numSegments; i++) {
            segments[i] = new Segment<>(segmentCapacity);
        }
    }

    private int getSegmentIndex(K key) {
        return Math.abs(key.hashCode()) % numberOfSegments;
    }

    public V get(K key) {
        return segments[getSegmentIndex(key)].get(key);
    }

    public void put(K key, V value) {
        segments[getSegmentIndex(key)].put(key, value);
    }


}

package com.cachesystem.cacheserver.persistence;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;


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

    public void print(){
        int i=0;
        for(Segment<K, V> s:segments){
            System.out.println("Segment "+i);
            for(Map.Entry<K,Node<K,V>> e:s.getMap().entrySet()){
                System.out.println(e.getKey()+" : "+e.getValue().getValue());
            }
            i++;
        }
    }


}

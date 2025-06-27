package com.cachesystem.cacheserver.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Node<K,V> {
    private K key;
    private V value;
    private Node<K,V> prev;
    private Node<K,V> next;
}

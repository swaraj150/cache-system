package com.cachesystem.cacheserver.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListNode {
    private String key;
    private ListNode prev;
    private ListNode next;
}

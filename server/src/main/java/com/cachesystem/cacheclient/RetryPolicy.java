package com.cachesystem.cacheclient;

import lombok.Data;

@Data
// Exponential backoff
public class RetryPolicy {
    public static final int attempts=5; // consecutive ones
    private static final long baseDelay=100L;

    public static long delayForAttempt(int n) {
        return baseDelay * (1L << n);
    }





}

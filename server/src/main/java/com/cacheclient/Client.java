package com.cacheclient;

import java.util.concurrent.CompletableFuture;

public interface Client {
    void connect() throws InterruptedException;
    CompletableFuture<Void> get(String key);
    CompletableFuture<Void> put(String key, String value);
    void shutdown();
}

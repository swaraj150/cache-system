package com.cachesystem.cacheserver.persistence;

import lombok.Data;

import java.nio.ByteBuffer;

public class AsyncFileSystem {
    public static String path;
    private final ByteBuffer buffer;

    public AsyncFileSystem(String path){
        this.buffer=ByteBuffer.allocate(1024);
        AsyncFileSystem.path=path;
    }




}

package com.cachesystem.cacheserver;

import com.cachesystem.cacheserver.persistence.SegmentedCache;

public class CacheStore {
    public static final SegmentedCache<String,Object> GLOBAL_CACHE=new SegmentedCache<>(64,16);

}

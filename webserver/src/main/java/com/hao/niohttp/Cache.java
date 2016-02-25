package com.hao.niohttp;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * Created by user on 2016/2/25.
 */
public class Cache {


    public static class CacheEntry{
        public byte[] header;
        public byte[] body;

        public CacheEntry(byte[] header, byte[] body) {
            this.header = header;
            this.body = body;
        }
    }

    /**
     * SoftReference会尽可能长的保留引用直到JVM内存不足时才会被回收
     * 所以非常适合缓存应用
     */
    public static class MapEntry extends SoftReference<CacheEntry>{

        /**
         *
         * Reference queues, to which registered reference objects are appended by the
         * garbage collector after the appropriate reachability changes are detected.
         *
         * @param referent
         * @param q
         */
        public MapEntry(CacheEntry referent, ReferenceQueue<? super CacheEntry> q) {
            super(referent, q);
        }
    }

}

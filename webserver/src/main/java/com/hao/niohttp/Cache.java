package com.hao.niohttp;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

        String key;

        /**
         *
         * Reference queues, to which registered reference objects are appended by the
         * garbage collector after the appropriate reachability changes are detected.
         *
         * @param referent
         * @param q
         */
        public MapEntry(String key,CacheEntry referent, ReferenceQueue<? super CacheEntry> q) {
            super(referent, q);
            this.key = key;
        }
    }

    private ReferenceQueue<CacheEntry> queue = new ReferenceQueue<>();

    private Map<String,MapEntry> map = new ConcurrentHashMap<>();

    public CacheEntry get(String key){
        CacheEntry result = null;
        MapEntry mapEntry = map.get(key);
        if(mapEntry != null){
            result = mapEntry.get();
            if(result == null){
                map.remove(mapEntry.key);
            }
        }
        return result;
    }

    private void processQueue(){
        MapEntry entry;
        while((entry = (MapEntry) queue.poll())!= null){
            map.remove(entry.key);
        }
    }

    public void put(String key,byte[] header,byte[] body){
        processQueue();
        map.put(key,new MapEntry(key,new CacheEntry(header,body),queue));
    }

    public static void main(String[] args) {
        String x1 = "\\[das!s";
        String x2 = "fdgda\\[das!sfsdfs";
		/*String[] x4 = x2.split("!s");
		for (int j = 0; j < x4.length; j++) {
			System.out.println(x4[i]);
		}
		System.out.println("----------------------------");*/
        String[] x3 = x2.split(x1);
        for (int j = 0; j < x3.length; j++) {
            System.out.println(x3[j]);
        }

    }



}

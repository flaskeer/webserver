package com.hao.niohttp;

import org.apache.log4j.Logger;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import static com.hao.niohttp.ResponseBuilder.*;
/**
 * Created by user on 2016/2/25.
 */
public class RequestHandler implements Runnable{

    private static final DateFormat formater = new SimpleDateFormat("HH:mm:ss");

    static {
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private static final Logger logger = Logger.getLogger(RequestHandler.class);
    private Cache cache;
    private File currentFile;
    private Date lastModified;
    private List<RequestSegmentHeader> pendingRequestSegment = new ArrayList<>();
    private Map<SocketChannel,RequestHeadHandler> requestMap = new WeakHashMap<>();
    private NioHttpServer server;
    private String serverRoot;
    private String acceptEncoding;


    public RequestHandler(NioHttpServer server, String root, Cache cache) {
        this.cache = cache;
        this.serverRoot = root;
        this.server = server;
    }

    public void processData(SocketChannel channel, byte[] data, int count) {
        byte[] dataCopy = new byte[count];
        System.arraycopy(data,0,dataCopy,0,count);
        synchronized (pendingRequestSegment){
            pendingRequestSegment.add(new RequestSegmentHeader(channel,dataCopy));
            pendingRequestSegment.notify();
        }
    }

    @Override
    public void run() {
        RequestSegmentHeader requestData = null;
        RequestHeadHandler header = null;
        Cache.CacheEntry entry = null;
        ResponseBuilder builder = new ResponseBuilder();
        byte[] head = null;
        byte[] body = null;
        String file = null;
        String mime = null;
        boolean zip = false;
        while (true){
            synchronized (pendingRequestSegment){
                while (pendingRequestSegment.isEmpty()){
                    try {
                        pendingRequestSegment.wait();
                    } catch (InterruptedException e) {
                    }
                }
                requestData = pendingRequestSegment.remove(0);
            }
            header = requestMap.get(requestData.client);
            if(header == null){
                header = new RequestHeadHandler();
                requestMap.put(requestData.client,header);
            }
            try{
            }catch (Exception e){
                builder.addHeader(CONTENT_LENGTH,0);
                builder.setStatus(SERVER_ERROR_500);
            }
        }
    }





}

class RequestSegmentHeader{
    SocketChannel client;
    byte[] data;

    public RequestSegmentHeader(SocketChannel client, byte[] data) {
        this.client = client;
        this.data = data;
    }
}

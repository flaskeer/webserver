package com.hao.niohttp;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by user on 2016/2/25.
 */
public class ResponseBuilder {

    public static final String OK = "HTTP/1.1 200 OK";
    public static final String NEW_LINE = "\r\n";
    public static final String NOT_FOUND_404 = "HTTP/1.1 404 NOT FOUND";
    public static final String SERVER_ERROR_500 = "HTTP/1.1 500 Internal Server Error";
    public static final String CONNECT_TYPE = "Content-Type";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String GZIP = "gzip";

    private String status;

    private Map<String,Object> header = new TreeMap<>();

    public ResponseBuilder() {
        status = OK;
    }

    public ResponseBuilder addHeader(String key,Object value){
        header.put(key,value);
        return this;
    }

    public void clear(){
        status = OK;
        header.clear();
    }

    public ResponseBuilder setStatus(String status){
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(status).append(NEW_LINE);
        header.forEach((k,v) ->{
            builder.append(k).append(":").append(v).append(NEW_LINE);
        });
        builder.append(NEW_LINE);
        return builder.toString();
    }
}

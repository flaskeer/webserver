package com.hao.niohttp;

import java.nio.channels.SocketChannel;

/**
 * Created by user on 2016/2/25.
 */
public class ChangeRequest {
    public static final int REGISTER = 1;
    public static final int CHANGEOPS = 2;

    public SocketChannel socketChannel;
    public int type;
    public int ops;

    public ChangeRequest(SocketChannel socketChannel, int type, int ops) {
        this.socketChannel = socketChannel;
        this.type = type;
        this.ops = ops;
    }
}

package com.hao.niohttp;

import com.hao.util.Util;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by user on 2016/2/25.
 */
public class NioHttpServer implements Runnable{

    private static Logger logger = Logger.getLogger(NioHttpServer.class);

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    private List<ChangeRequest> changeRequests = new LinkedList<>();
    private Map<SocketChannel,List<ByteBuffer>> pendingSent = new HashMap<>();
    private List<RequestHandler> requestHandlers = new ArrayList<>();

    public NioHttpServer(InetAddress address,int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(address,port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void accept(SelectionKey key) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        logger.info("new connection:\t" + socketChannel);
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
    }

    public void addRequestHandler(RequestHandler handler){
        requestHandlers.add(handler);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        readBuffer.clear();
        int numRead;
        try {
            numRead = channel.read(readBuffer);
        } catch (IOException e) {
            key.cancel();
            channel.close();
            logger.info("close by exception:" + channel);
            return;
        }
        if(numRead == -1){
            channel.close();
            key.channel();
            logger.info("close by shutdown:" + channel);
            return;
        }
        int worker = channel.hashCode() % requestHandlers.size();
        if(logger.isDebugEnabled()){
            logger.debug(selector.keys().size() + "\t" + worker + channel);
        }
        requestHandlers.get(worker).processData(channel,readBuffer.array(),numRead);
    }

    public void send(SocketChannel socketChannel,byte[] data){
        synchronized (changeRequests){
            changeRequests.add(new ChangeRequest(socketChannel,ChangeRequest.CHANGEOPS,SelectionKey.OP_WRITE));
            synchronized (pendingSent){
                List<ByteBuffer> queue = pendingSent.get(socketChannel);
                if(queue == null){
                    queue = new ArrayList<>();
                    pendingSent.put(socketChannel,queue);
                }
                queue.add(ByteBuffer.wrap(data));
            }
        }
        selector.wakeup();
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        synchronized (pendingSent){
            List<ByteBuffer> queue = pendingSent.get(socketChannel);
            while (!queue.isEmpty()){
                ByteBuffer buffer = queue.get(0);
                socketChannel.write(buffer);
                if(buffer.remaining() > 0){
                    break;
                }
                queue.remove(0);
            }
            if(queue.isEmpty()){
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }


    @Override
    public void run() {
        SelectionKey key = null;
        while (true){
            try{
                synchronized (changeRequests){
                    for (ChangeRequest request : changeRequests) {
                        switch (request.type){
                            case ChangeRequest.CHANGEOPS:
                                key = request.socketChannel.keyFor(selector);
                                if(key != null && key.isValid()){
                                    key.interestOps(request.ops);
                                }
                                break;
                        }
                    }
                    changeRequests.clear();
                }
                selector.select();
                Iterator<SelectionKey> selectKeys = selector.selectedKeys().iterator();
                while(selectKeys.hasNext()){
                    key = selectKeys.next();
                    if(!key.isValid()){
                        continue;
                    }
                    if(key.isAcceptable()){
                        accept(key);
                    }else if(key.isReadable()){
                        read(key);
                    }else if(key.isWritable()){
                        write(key);
                    }
                }
            }catch (Exception e){
                if(key != null){
                    key.cancel();
                    Util.close(key.channel());
                }
                logger.error("closed:" + key.channel(),e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String root = new File(".").getAbsolutePath();
        int port = 8080;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        if(args.length > 1){
            root = args[1];
        }
        System.out.println(port + "\t" + root);
        NioHttpServer server = new NioHttpServer(null,port);
        int cpu = Runtime.getRuntime().availableProcessors();
        Cache cache = new Cache();
        for (int i = 0; i < cpu; i++) {
            RequestHandler handler = new RequestHandler(server,root,cache);
            server.addRequestHandler(handler);
            new Thread(handler,"worker" + i).start();
        }
        new Thread(server,"selector").start();
    }
}

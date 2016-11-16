package com.server.nio;

import com.server.ByteBufferPool;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhen on 16/10/11.
 */
public class NioHttpServer {

    private static final Logger logger = Logger.getLogger(NioHttpServer.class);

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private ExecutorService executorService;

    private int port;

    public static boolean shutFlag = false;

    private static final long tcpSelectorTimeout = 10000;

    public NioHttpServer(int port) {
        this.port = port;
    }

    private void listen() throws Exception {
        ByteBufferPool byteBufferPool = new ByteBufferPool(100, 256);
        ServerSocketChannel server = ServerSocketChannel.open();
        Selector selector = Selector.open();
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        //刚开始就注册链接事件
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (!shutFlag && selector!=null) {
            try {
                if(selector.select(tcpSelectorTimeout)==0) {
                    continue;
                }
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();

                    if(key.isAcceptable()) {
                        ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
                        SocketChannel channel = serverSocket.accept();
                        channel.configureBlocking(false);
                        //网络管道准备处理读事件
                        channel.register(selector, SelectionKey.OP_READ);
                    }

                    if(key.isReadable()) {
                        key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                        key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                        executorService.execute(new HandlerClient(key, byteBufferPool));
                    }else {
                        key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                    }

                    iter.remove();
                }
            } catch (Exception e) {
                logger.error("处理套接字异常", e);
            }
        }

        server.close();
        if (selector != null) {
            selector.close();
        }
    }

    public void start() {
        try {
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            listen();
            executorService.shutdown();
        }catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new NioHttpServer(8080).start();
    }
}

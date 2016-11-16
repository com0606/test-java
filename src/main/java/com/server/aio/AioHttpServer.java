package com.server.aio;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhen on 16/10/17.
 */
public class AioHttpServer {

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private int port;

    public static boolean shutFlag = false;

    public AioHttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            listen();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listen() throws Exception {
        final AsynchronousServerSocketChannel aserver = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
        aserver.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                final ByteBuffer buffer = ByteBuffer.allocate(1024);
                try {
                    buffer.clear();
                    result.read(buffer);
                    buffer.flip();
                    System.out.println("received message: " + Charset.forName("UTF-8").decode(buffer).toString());
                } catch (Exception e) {
                    System.out.println(e.toString());
                } finally {
                    try {
                        result.close();
                        aserver.accept(null, this);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("fail..." + exc);
            }
        });
    }

    public static void main(String[] args) {
        new AioHttpServer(8080).start();
    }
}

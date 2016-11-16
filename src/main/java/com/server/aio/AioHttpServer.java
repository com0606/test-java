package com.server.aio;

import org.apache.log4j.Logger;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhen on 16/10/17.
 */
public class AioHttpServer {

    private static final Logger logger = Logger.getLogger(AioHttpServer.class);

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    public static boolean shutFlag = false;

    private int port;

    private AcceptHandler acceptHandler;

    private AsynchronousServerSocketChannel server;

    public AioHttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            listen();
        }catch (Exception e) {
            logger.error("启动异常", e);
        }
    }

    public void accept() {
        if(!shutFlag) {
            server.accept(this, acceptHandler);
        }
    }

    private void listen() throws Exception {
        acceptHandler = new AcceptHandler();
        final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(service, 1);
        server = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(port), 100);
        accept();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if(!shutFlag) {
                            Thread.sleep(2000);
                        }else {
                            server.close();
                            group.shutdown();
                            service.shutdownNow();
                            break;
                        }
                    }
                }catch (Exception e) {
                    logger.error("stop error", e);
                }
            }
        };
        new Thread(run, "monitor thread").start();
    }

    public static void main(String[] args) {
        new AioHttpServer(8080).start();
    }
}

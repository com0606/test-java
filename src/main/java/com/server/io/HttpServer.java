package com.server.io;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * HttpServer
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-27
 */
public class HttpServer {

    private static final Logger logger = Logger.getLogger(HttpServer.class);

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private int port;

    public static boolean shutFlag = false;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            logger.debug(e.getMessage());
            return;
        }
        logger.info("starting...");
        while (!shutFlag) {
            try {
                Socket socket = server.accept();
                Runnable runner = new Handler(socket);
                executorService.execute(runner);
            } catch (IOException e) {
                logger.debug(e.getMessage());
            }
        }

        try {
            server.close();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        new HttpServer(8888).start();
    }
}

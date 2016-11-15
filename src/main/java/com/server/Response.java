package com.server;

/**
 * <p>
 * Response
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-28
 */
public abstract class Response {
    protected static final String SHUTDOWN_COMMAND = "/shutdown";
    protected static final int BUFFER_SIZE = 1024;

    protected String message = "HTTP/1.1 200 OK\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: 20\r\n" +
            "\r\n" +
            "<h1>shutdown...</h1>";

    protected String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: 23\r\n" +
            "\r\n" +
            "<h1>File Not Found</h1>";

    protected String requestUri;

    public abstract void sendStaticResource();
}

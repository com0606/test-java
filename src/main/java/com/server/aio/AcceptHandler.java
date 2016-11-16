package com.server.aio;

import org.apache.log4j.Logger;

import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by lizhen on 2016-11-15.
 */
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioHttpServer> {

    private static final Logger logger = Logger.getLogger(AcceptHandler.class);

    @Override
    public void completed(AsynchronousSocketChannel result, AioHttpServer attachment) {
        try {
            logger.info("remote address" + result.getRemoteAddress());

            result.setOption(StandardSocketOptions.TCP_NODELAY, true);
            result.setOption(StandardSocketOptions.SO_SNDBUF, 1024);
            result.setOption(StandardSocketOptions.SO_RCVBUF, 1024);

            if(result.isOpen()) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.clear();
                result.read(buffer, result, new AioRequest(buffer));
            }
        } catch (Exception e) {
            logger.error("accept处理异常", e);
        } finally {
            //复用
            attachment.getServer().accept(null, this);
        }
    }

    @Override
    public void failed(Throwable exc, AioHttpServer attachment) {
        logger.error("fail..." + exc);
        attachment.getServer().accept(attachment, this);
    }
}

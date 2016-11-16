package com.server.aio;

import com.server.ByteBufferPool;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by lizhen on 2016-11-15.
 */
public class WriteHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {

    private static final Logger logger = Logger.getLogger(WriteHandler.class);

    private ByteBuffer buffer;

    private ByteBufferPool pool;

    public WriteHandler(ByteBuffer buffer, ByteBufferPool pool) {
        this.buffer = buffer;
        this.pool = pool;
    }

    @Override
    public void completed(Integer result, AsynchronousSocketChannel attachment) {
        close(attachment);
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
        close(attachment);
    }

    private void close(AsynchronousSocketChannel attachment) {
        try {
            pool.returnByteBuffer(buffer);
            //连接关闭
            attachment.close();
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }
}

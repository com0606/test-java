package com.server.aio;

import com.server.ByteBufferPool;
import org.apache.log4j.Logger;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by lizhen on 2016-11-15.
 */
public class ReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel>  {

    private static final Logger logger = Logger.getLogger(ReadHandler.class);

    private ByteBuffer buffer;

    private ByteBufferPool pool;

    public ReadHandler(ByteBuffer buffer, ByteBufferPool pool) {
        this.buffer = buffer;
        this.pool = pool;
    }

    @Override
    public void completed(Integer result, AsynchronousSocketChannel attachment) {
        if(result < 0) {
            close(attachment);
            return;
        }

        AioRequest request = new AioRequest(buffer);
        String uri = request.parse();

        AioResponse response = new AioResponse(buffer, uri);
        response.sendStaticResource();

        attachment.write(buffer, attachment, new WriteHandler(buffer, pool));
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

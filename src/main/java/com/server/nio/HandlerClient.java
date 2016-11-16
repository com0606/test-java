package com.server.nio;

import com.server.ByteBufferPool;
import com.server.Request;
import com.server.Response;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by lizhen on 16/10/13.
 */
public class HandlerClient implements Runnable {

    private static final Logger logger = Logger.getLogger(HandlerClient.class);

    private SelectionKey key;

    private ByteBufferPool pool;

    public HandlerClient(SelectionKey key, ByteBufferPool pool) {
        this.key = key;
        this.pool = pool;
    }

    @Override
    public void run() {
        ByteBuffer buffer = null;
        try {
            buffer = pool.borrowByteBuffer();
            drainChannel(buffer);
        }catch (Exception e) {
            try {
                key.channel().close();
                key.selector().wakeup();
            }catch (Exception e1) {
                logger.debug(e1.getMessage());
            }
            logger.debug(e.getMessage());
        } finally {
            if(buffer!=null) {
                try {
                    pool.returnByteBuffer(buffer);
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            }
        }
    }

    private void drainChannel(ByteBuffer buffer) throws Exception {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        Request request = new NioRequest(buffer, socketChannel);
        String uri = request.parse();

        if(!socketChannel.isOpen()) {
            return;
        }

        Thread.sleep(2000);
        Response response = new NioResponse(uri, socketChannel, buffer);
        response.sendStaticResource();

        socketChannel.close();

//        key.interestOps(key.interestOps() | SelectionKey.OP_READ);
//        key.selector().wakeup();
    }
}

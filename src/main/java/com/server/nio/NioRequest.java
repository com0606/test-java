package com.server.nio;

import com.server.Request;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <p>
 * Request
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-28
 */
public class NioRequest extends Request {

    private static final Logger logger = Logger.getLogger(NioRequest.class);

    private SocketChannel socketChannel;

    public NioRequest(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    private String uri;

    public String parse() {
        try {
            int count;
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.clear();
            byte[] request = new byte[0];
            while ((count = socketChannel.read(buffer)) > 0) {
                buffer.flip();
                byte[] newbuf = new byte[request.length + count];
                System.arraycopy(request, 0, newbuf, 0, request.length);

                if ( buffer.hasArray() ) {
                    System.arraycopy(buffer.array(), 0, newbuf, request.length, count);
                }else {
                    buffer.get(newbuf, request.length, count);
                }
                request = newbuf;
                buffer.clear();
            }

            if(count < 0) {
                socketChannel.close();
                return uri;
            }

            uri = new String(request, "UTF-8");
        } catch (Exception e) {
            logger.error("解析异常", e);
        }
        logger.debug(uri);
        uri = parseUri(uri);
        logger.debug("解析后uri" + uri);
        return uri;
    }
}

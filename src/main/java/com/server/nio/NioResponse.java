package com.server.nio;

import com.server.Response;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * <p>
 * Response
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-28
 */
public class NioResponse extends Response {

    private static final Logger logger = Logger.getLogger(NioResponse.class);

    private SocketChannel socketChannel;

    private ByteBuffer buffer;

    public NioResponse(String requestUri, SocketChannel socketChannel, ByteBuffer buffer) {
        this.requestUri = requestUri;
        this.socketChannel = socketChannel;
        this.buffer = buffer;
    }

    public void sendStaticResource() {
        FileChannel fis = null;
        try {
            if(requestUri==null) {
                socketChannel.write(ByteBuffer.wrap(errorMessage.getBytes()));
                return;
            }

            File file = new File(NioHttpServer.WEB_ROOT, requestUri);
            if (!"/".equals(requestUri) && file.exists()) {
                fis = new FileInputStream(file).getChannel();
                buffer.clear();
                while (fis.read(buffer) != -1) {
                    buffer.flip();
                    socketChannel.write(buffer);
                    buffer.clear();
                }
            } else if(SHUTDOWN_COMMAND.equals(requestUri)) {
                NioHttpServer.shutFlag = true;
                socketChannel.write(ByteBuffer.wrap(message.getBytes()));
            } else {
                // file not found
                socketChannel.write(ByteBuffer.wrap(errorMessage.getBytes()));
            }
        } catch (Exception e) {
            logger.error("uri请求处理异常", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.debug(e.getMessage());
                }
            }
        }
    }
}

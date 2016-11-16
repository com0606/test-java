package com.server.aio;

import com.server.Response;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by lizhen on 2016-11-16.
 */
public class AioResponse extends Response {

    private static final Logger logger = Logger.getLogger(AioResponse.class);

    private ByteBuffer buffer;

    public AioResponse(ByteBuffer buffer, String requestUri) {
        this.buffer = buffer;
        this.requestUri = requestUri;
    }

    @Override
    public void sendStaticResource() {
        buffer.clear();
        FileChannel fis = null;
        try {
            if(requestUri==null) {
                buffer.put(errorMessage.getBytes());
                return;
            }

            File file = new File(AioHttpServer.WEB_ROOT, requestUri);
            if (!"/".equals(requestUri) && file.exists()) {
                fis = new FileInputStream(file).getChannel();
                fis.read(buffer);
            } else if(SHUTDOWN_COMMAND.equals(requestUri)) {
                AioHttpServer.shutFlag = true;
                buffer.put(message.getBytes());
            } else {
                // file not found
                buffer.put(errorMessage.getBytes());
            }
        } catch (Exception e) {
            logger.error("uri请求处理异常", e);
        } finally {
            buffer.flip();
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

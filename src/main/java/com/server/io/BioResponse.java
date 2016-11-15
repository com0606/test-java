package com.server.io;


import com.server.Response;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * BioResponse
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-28
 */
public class BioResponse extends Response {

    private static final Logger logger = Logger.getLogger(BioResponse.class);

    private OutputStream output;

    public BioResponse(String requestUri, OutputStream output) {
        this.requestUri = requestUri;
        this.output = output;
    }

    public void sendStaticResource() {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            if (requestUri == null) {
                output.write(errorMessage.getBytes());
                return;
            }

            File file = new File(HttpServer.WEB_ROOT, requestUri);
            if (!"/".equals(requestUri) && file.exists()) {
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    output.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            } else if(SHUTDOWN_COMMAND.equals(requestUri)) {
                HttpServer.shutFlag = true;
                output.write(message.getBytes());
            } else {
                // file not found
                output.write(errorMessage.getBytes());
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

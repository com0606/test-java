package com.server.io;

import com.server.Request;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * BioRequest
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-28
 */
public class BioRequest extends Request {

    private static final Logger logger = Logger.getLogger(BioRequest.class);

    private InputStream input;

    public BioRequest(InputStream input) {
        this.input = input;
    }

    public String parse() {
        // Read a set of characters from the socket
        StringBuilder request = new StringBuilder();
        int i;
        byte[] buffer = new byte[1024];
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            logger.error("读取异常", e);
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }
        logger.debug(request.toString());
        String uri = parseUri(request.toString());
        logger.debug("解析后uri:" + uri);

        return uri;
    }
}

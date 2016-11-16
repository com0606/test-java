package com.server.aio;

import com.server.Request;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by lizhen on 2016-11-16.
 */
public class AioRequest extends Request {

    private static final Logger logger = Logger.getLogger(AioRequest.class);

    private ByteBuffer buffer;

    public AioRequest(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public String parse() {
        buffer.flip();
        String request = Charset.forName("UTF-8").decode(buffer).toString();
        logger.debug(request);
        String uri = parseUri(request);
        logger.debug("解析后uri:" + uri);
        return uri;
    }
}

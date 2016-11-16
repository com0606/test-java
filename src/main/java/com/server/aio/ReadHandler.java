package com.server.aio;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

/**
 * Created by lizhen on 2016-11-15.
 */
public class ReadHandler implements CompletionHandler<Integer, AsynchronousSocketChannel>  {

    private static final Logger logger = Logger.getLogger(ReadHandler.class);

    private ByteBuffer buffer;

    public ReadHandler(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void completed(Integer result, AsynchronousSocketChannel attachment) {
        if(result < 0) {
            try {
                //连接关闭
                attachment.close();
            } catch (IOException e) {
                logger.debug(e.getMessage());
            }
        }else if (result==0) {
            logger.info("no data");
        }else {
            buffer.flip();
            String request = Charset.forName("UTF-8").decode(buffer).toString();
            logger.debug(request);
            String uri = parseUri(request);
            logger.debug("解析后uri:" + uri);

        }
    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

    }

//    private ByteBuffer parseStaticResource() {
//        try {
//            if(requestUri==null) {
//                socketChannel.write(ByteBuffer.wrap(errorMessage.getBytes()));
//                return;
//            }
//
//            File file = new File(NioHttpServer.WEB_ROOT, requestUri);
//            if (!"/".equals(requestUri) && file.exists()) {
//                fis = new FileInputStream(file).getChannel();
//                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
//                while (fis.read(buffer) != -1) {
//                    buffer.flip();
//                    socketChannel.write(buffer);
//                    buffer.clear();
//                }
//            } else if(SHUTDOWN_COMMAND.equals(requestUri)) {
//                NioHttpServer.shutFlag = true;
//                socketChannel.write(ByteBuffer.wrap(message.getBytes()));
//            } else {
//                // file not found
//                socketChannel.write(ByteBuffer.wrap(errorMessage.getBytes()));
//            }
//        } catch (Exception e) {
//            logger.error("uri请求处理异常", e);
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}

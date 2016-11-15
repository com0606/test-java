package com.server.io;

import com.server.Request;
import com.server.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <p>
 * Handler
 * </p>
 *
 * @author zhen.li
 * @date 2016-07-28
 */
public class Handler implements Runnable {

    private static final Logger logger = Logger.getLogger(Handler.class);

    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();

            Request request = new BioRequest(input);
            String uri = request.parse();

            OutputStream out = socket.getOutputStream();
            Response response = new BioResponse(uri, out);
            response.sendStaticResource();

            input.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }
}

package com.server.aio;

import com.server.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Created by lizhen on 2016-11-15.
 */
public class WriteHandler extends Response implements CompletionHandler<AsynchronousSocketChannel, AioHttpServer> {

    private String uri;

    private ByteBuffer buffer;

    @Override
    public void completed(AsynchronousSocketChannel result, AioHttpServer attachment) {

    }

    @Override
    public void failed(Throwable exc, AioHttpServer attachment) {

    }

    @Override
    public void sendStaticResource() {

    }
}

package com.server;

import org.apache.commons.pool.PoolableObjectFactory;

import java.nio.ByteBuffer;

/**
 * Created by lizhen on 2016-11-16.
 */
public class ByteBufferFactory implements PoolableObjectFactory<ByteBuffer> {

    private int capacity;

    public ByteBufferFactory(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public ByteBuffer makeObject() throws Exception {
        return ByteBuffer.allocate(capacity);
    }

    @Override
    public void destroyObject(ByteBuffer byteBuffer) throws Exception {
        byteBuffer.clear();
    }

    @Override
    public boolean validateObject(ByteBuffer byteBuffer) {
        return false;
    }

    @Override
    public void activateObject(ByteBuffer byteBuffer) throws Exception {

    }

    @Override
    public void passivateObject(ByteBuffer byteBuffer) throws Exception {

    }
}

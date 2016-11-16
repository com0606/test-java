package com.server;

import org.apache.commons.pool.impl.GenericObjectPool;

import java.nio.ByteBuffer;

/**
 * Created by lizhen on 2016-11-16.
 */
public class ByteBufferPool {

    private GenericObjectPool<ByteBuffer> pool;

    public ByteBufferPool(int maxActive, int capacity) {
        pool = new GenericObjectPool<ByteBuffer>(new ByteBufferFactory(capacity), maxActive, (byte)0, 1000, 8, 0, false, false, 6, 3, 900000L, false);
    }

    public ByteBuffer borrowByteBuffer() throws Exception {
        return pool.borrowObject();
    }

    public void returnByteBuffer(ByteBuffer byteBuffer) throws Exception {
        pool.returnObject(byteBuffer);
    }
}

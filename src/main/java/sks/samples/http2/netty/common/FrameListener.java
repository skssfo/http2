/*
 * FrameListener.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.common;

import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2EventAdapter;

public class FrameListener extends Http2EventAdapter {

    private Http2ConnectionEncoder encoder;
    private Http2Connection connection;

    public void setEncoder(Http2ConnectionEncoder encoder) {
        this.encoder = encoder;
    }

    public void setConnection(Http2Connection connection) {
        this.connection = connection;
    }

    public Http2ConnectionEncoder getEncoder() {
        return encoder;
    }

    public Http2Connection getConnection() {
        return connection;
    }
}
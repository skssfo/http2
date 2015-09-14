/*
 * AbstractRequestHandlerBase.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.server.handler;

import io.netty.handler.codec.http2.Http2Connection;

public abstract class AbstractRequestHandlerBase implements RequestHandler {

    private Http2Connection connection;

    @Override
    public void setConnection(Http2Connection connection) {
        this.connection = connection;
    }

    @Override
    public Http2Connection getConnection() {
        return connection;
    }
}

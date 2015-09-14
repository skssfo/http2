/*
 * UnknownRequestHandler.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2Headers;

public class UnknownRequestHandler extends AbstractRequestHandlerBase {

    @Override
    public void handleHeaderFrame(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding,
                                  boolean endStream, Http2ConnectionEncoder encoder) {

    }

    @Override
    public void handleDataFrame(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding,
                                boolean endOfStream) {

    }
}

/*
 * SettingsHandler.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http2.Http2Settings;

import java.util.concurrent.TimeUnit;

final class SettingsHandler extends SimpleChannelInboundHandler<Http2Settings> {

    private final ChannelPromise promise;

    SettingsHandler(ChannelPromise promise) {
        this.promise = promise;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Http2Settings msg) throws Exception {
        promise.setSuccess();
        //No need for this handler to be in the chain after the initial settings exchange.
        ctx.pipeline().remove(this);
    }

    /**
     * Wait for settings exchange to complete between the client and server.
     * @param timeout
     * @param unit
     * @throws Exception
     */
    public void awaitSettings(long timeout, TimeUnit unit) throws Exception {
        if (!promise.awaitUninterruptibly(timeout, unit)) {
            throw new IllegalStateException("Timed out waiting for settings");
        }
        if (!promise.isSuccess()) {
            throw new RuntimeException(promise.cause());
        }
        System.err.println("Settings exchange complete");
    }
}

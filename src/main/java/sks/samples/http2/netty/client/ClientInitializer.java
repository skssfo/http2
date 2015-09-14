/*
 * ClientInitializer.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import sks.samples.http2.netty.common.ConnectionHandler;

final class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;
    private SettingsHandler settingsHandler;

    ClientInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(sslContext.newHandler(ch.alloc()),
                new ConnectionHandler(new ClientFrameListener(), false));

        settingsHandler = new SettingsHandler(ch.newPromise());
        pipeline.addLast(settingsHandler);
    }

    SettingsHandler getSettingsHandler() {
        return settingsHandler;
    }
}

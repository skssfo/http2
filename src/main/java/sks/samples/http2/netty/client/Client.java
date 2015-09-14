/*
 * Client.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import sks.samples.http2.netty.util.Util;

import java.util.concurrent.TimeUnit;

public final class Client {

    public static void main(String[] args) throws Exception {

        final String host = System.getProperty("serverHost", "127.0.0.1");
        final int port = Integer.getInteger("serverPort", 8443);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        SslContext sslContext = Util.getSslContext(false);
        ClientInitializer initializer = new ClientInitializer(sslContext);
        try {
            // Configure the client.
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.remoteAddress(host, port);
            b.handler(initializer);

            System.err.println("Connecting to " + host + ":" + port);
            // Start the client.
            Channel channel = b.connect().syncUninterruptibly().channel();

            // Wait for the settings exchange to complete.
            SettingsHandler http2SettingsHandler = initializer.getSettingsHandler();
            http2SettingsHandler.awaitSettings(5, TimeUnit.SECONDS);
            System.out.println("Client.main() Settings exchange complete");

            while (true) { System.in.read(); }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}

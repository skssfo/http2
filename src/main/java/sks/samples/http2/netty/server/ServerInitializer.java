
package sks.samples.http2.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import sks.samples.http2.netty.common.ConnectionHandler;


public final class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;

    public ServerInitializer(SslContext sslCtx) {
        this.sslContext = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline().addLast(sslContext.newHandler(channel.alloc()),
                new ConnectionHandler(new ServerFrameListener(), true));
    }
}

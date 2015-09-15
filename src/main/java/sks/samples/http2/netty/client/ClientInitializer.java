
package sks.samples.http2.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import sks.samples.http2.netty.common.ConnectionHandler;
import sks.samples.http2.netty.common.FrameListener;

final class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslContext;
    private SettingsHandler settingsHandler;
    private ClientFrameListener clientFrameListener;

    ClientInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        clientFrameListener = new ClientFrameListener();
        pipeline.addLast(sslContext.newHandler(ch.alloc()),
                new ConnectionHandler(clientFrameListener, false));

        settingsHandler = new SettingsHandler(ch.newPromise());
        pipeline.addLast(settingsHandler);
    }

    SettingsHandler getSettingsHandler() {
        return settingsHandler;
    }

    ClientFrameListener getClientFrameListener() {
        return clientFrameListener;
    }
}

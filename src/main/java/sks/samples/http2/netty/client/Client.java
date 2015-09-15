
package sks.samples.http2.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.handler.ssl.SslContext;
import io.netty.util.ByteString;
import io.netty.util.CharsetUtil;
import sks.samples.http2.netty.common.SupportedPath;
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

            //sendRequestToReceiveTimerEventsInSameStream(initializer, channel);

            sendRequestToReceiveTimerEventsAsPushPromise(initializer, channel);

            while (true) { System.in.read(); }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    private static void sendRequestToReceiveTimerEventsAsPushPromise(ClientInitializer initializer, Channel channel)
            throws Http2Exception {

        Http2Headers headers = getCommonHeaders();
        headers.add(Http2Headers.PseudoHeaderName.PATH.value(),
                new ByteString(SupportedPath.PUSH_PROMISE, CharsetUtil.UTF_8));

        sendHeader(initializer, channel, headers, true);
    }

    private static void sendRequestToReceiveTimerEventsInSameStream(ClientInitializer initializer, Channel channel)
            throws Http2Exception {

        Http2Headers headers = getCommonHeaders();
        headers.add(Http2Headers.PseudoHeaderName.PATH.value(),
                new ByteString(SupportedPath.TIMER_EVENT, CharsetUtil.UTF_8));

        sendHeader(initializer, channel, headers, true);
    }

    private static Http2Headers getCommonHeaders() {
        //Create the header
        Http2Headers headers = new DefaultHttp2Headers();
        headers.add(Http2Headers.PseudoHeaderName.METHOD.value(), new ByteString("GET", CharsetUtil.UTF_8));
        headers.add(Http2Headers.PseudoHeaderName.SCHEME.value(), new ByteString("https", CharsetUtil.UTF_8));
        headers.add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
        headers.add(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.DEFLATE);

        return headers;
    }

    private static void sendHeader(ClientInitializer initializer, Channel channel, Http2Headers headers,
                                   boolean endOfStream) throws Http2Exception {
        //Create a new stream
        ClientFrameListener listener = initializer.getClientFrameListener();
        Http2Connection connection = listener.getConnection();
        int streamId = listener.getConnection().local().nextStreamId();
        System.out.println("Next stream Id on the client side " + streamId);

        Http2Stream stream = connection.local().createStream(streamId, false);
        System.err.println("Created new stream - state " + stream.state() + " id " + stream.id());

        Http2ConnectionEncoder encoder = listener.getEncoder();
        ChannelHandlerContext ctx = listener.getChannelHandlerContext();
        encoder.writeHeaders(ctx, streamId, headers, 0, endOfStream, channel.newPromise());
    }
}


package sks.samples.http2.netty.common;

import io.netty.handler.codec.http2.DefaultHttp2Connection;
import io.netty.handler.codec.http2.DefaultHttp2FrameReader;
import io.netty.handler.codec.http2.DefaultHttp2FrameWriter;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.codec.http2.Http2FrameLogger;
import io.netty.handler.codec.http2.Http2FrameReader;
import io.netty.handler.codec.http2.Http2FrameWriter;
import io.netty.handler.codec.http2.Http2InboundFrameLogger;
import io.netty.handler.codec.http2.Http2OutboundFrameLogger;

import static io.netty.handler.logging.LogLevel.INFO;

public class ConnectionHandler extends Http2ConnectionHandler {

    public static final Http2FrameLogger logger = new Http2FrameLogger(INFO, ConnectionHandler.class);


    public ConnectionHandler(FrameListener frameListener, boolean isServer) {

        this(new DefaultHttp2Connection(isServer),
                new Http2InboundFrameLogger(new DefaultHttp2FrameReader(), logger),
                new Http2OutboundFrameLogger(new DefaultHttp2FrameWriter(), logger),
                frameListener);
    }

    private ConnectionHandler(Http2Connection connection, Http2FrameReader reader,
                              Http2FrameWriter writer, FrameListener frameListener) {

        super(connection, reader, writer, frameListener);
        frameListener.setConnection(connection());
        frameListener.setEncoder(encoder());
    }

}


package sks.samples.http2.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2Headers;

public interface RequestHandler {

    void handleHeaderFrame(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding,
                           boolean endStream, Http2ConnectionEncoder encoder);

    void handleDataFrame(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream);

    void setConnection(Http2Connection connection);

    Http2Connection getConnection();
}

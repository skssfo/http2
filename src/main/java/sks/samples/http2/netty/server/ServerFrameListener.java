
package sks.samples.http2.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.util.ByteString;
import io.netty.util.CharsetUtil;
import sks.samples.http2.netty.common.FrameListener;
import sks.samples.http2.netty.server.handler.RequestHandler;
import sks.samples.http2.netty.server.handler.RequestHandlerFactory;

final class ServerFrameListener extends FrameListener {

    @Override
    public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
            throws Http2Exception {

        System.out.println("ServerFrameListener.onDataRead()");
        return super.onDataRead(ctx, streamId, data, padding, endOfStream);
    }

    @Override
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding,
                              boolean endStream) throws Http2Exception {

        System.out.println("ServerFrameListener.onHeadersRead()");
        ByteString path = headers.get(Http2Headers.PseudoHeaderName.PATH.value());
        RequestHandler handler =
                RequestHandlerFactory.getInstance().getRequestHandler(path.toString(CharsetUtil.UTF_8), getConnection());
        handler.handleHeaderFrame(ctx, streamId, headers, padding, endStream, getEncoder());
    }

    @Override
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency,
                              short weight, boolean exclusive, int padding, boolean endStream) throws Http2Exception {

        onHeadersRead(ctx, streamId, headers, padding, endStream);
    }
}

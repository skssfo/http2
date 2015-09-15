
package sks.samples.http2.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Settings;
import sks.samples.http2.netty.common.FrameListener;

public class ClientFrameListener extends FrameListener {


    @Override
    public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
            throws Http2Exception {

        System.out.println("ClientFrameListener.onDataRead()");
        return super.onDataRead(ctx, streamId, data, padding, endOfStream);
    }

    @Override
    public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding,
                              boolean endStream) throws Http2Exception {

        System.out.println("ClientFrameListener.onHeadersRead()");
        super.onHeadersRead(ctx, streamId, headers, padding, endStream);
    }

    @Override
    public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings)
            throws Http2Exception {
        //save the reference to ChannelHandlerContext
        setChannelHandlerContext(ctx);
        ctx.fireChannelRead(settings);
    }

    @Override
    public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId,
                                  Http2Headers headers, int padding) throws Http2Exception {

        System.out.println("ClientFrameListener.onHeadersRead()");
        super.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
    }
}

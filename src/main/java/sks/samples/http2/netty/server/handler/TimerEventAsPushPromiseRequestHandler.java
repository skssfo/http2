/*
 * TimerEventAsPushPromiseRequestHandler.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.buffer.Unpooled.unreleasableBuffer;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class TimerEventAsPushPromiseRequestHandler extends AbstractRequestHandlerBase {

    @Override
    public void handleHeaderFrame(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding,
                                  boolean endStream, Http2ConnectionEncoder encoder) {

        System.out.println("TimerEventAsPushPromise request received on stream " + streamId);

        //send a response header first followed by data frames.
        Http2Headers responseHeaders = new DefaultHttp2Headers();
        responseHeaders.status(OK.codeAsText());
        encoder.writeHeaders(ctx, streamId, responseHeaders, 0, false, ctx.newPromise());
        //send data frame every 5 seconds after the initial delay of 1 second
        ctx.channel().eventLoop().scheduleAtFixedRate(
                new PushPromiseSender(ctx, streamId, encoder, getConnection()),
                1000, 5000, TimeUnit.MILLISECONDS);


    }

    @Override
    public void handleDataFrame(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding,
                                boolean endOfStream) {

    }

    private static class PushPromiseSender implements Runnable {

        ChannelHandlerContext ctx;
        int streamId;
        Http2ConnectionEncoder encoder;
        Http2Connection connection;

        public PushPromiseSender (ChannelHandlerContext ctx,
                               int streamId,
                               Http2ConnectionEncoder encoder,
                               Http2Connection connection) {
            this.ctx = ctx;
            this.streamId = streamId;
            this.encoder = encoder;
            this.connection = connection;

        }

        @Override
        public void run() {
            System.out.println("Timer triggered - Thread - " + Thread.currentThread().getName());
            ChannelPromise channelPromise = ctx.newPromise();
            channelPromise.addListener(new GenericFutureListener<Future<Void>>() {
                @Override
                public void operationComplete(Future<Void> future) throws Exception {
                    System.out.println("Operation complete callback - " + future.isSuccess() +
                            " Thread - " + Thread.currentThread().getName());
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    }
                }
            });

            try {
                sendAPushPromise();
                encoder.flowController().writePendingBytes();
                ctx.flush();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        private void sendAPushPromise() throws Http2Exception {

            int pushPromiseStreamId = connection.local().nextStreamId();
            encoder.writePushPromise(ctx, streamId, pushPromiseStreamId,
                    new DefaultHttp2Headers().status(OK.codeAsText()), 0, ctx.newPromise());

            //Http2Stream stream = connection.local().reservePushStream(pushPromiseStreamId, connection.connectionStream());
            Http2Headers headers = new DefaultHttp2Headers();
            headers.status(OK.codeAsText());
            System.out.println("Encoder " + encoder.getClass().getName());
            ByteBuf payload = unreleasableBuffer(copiedBuffer("PushMessage : " + new Date().toString(), CharsetUtil.UTF_8));
            encoder.writeHeaders(ctx, pushPromiseStreamId, headers, 0, false, ctx.newPromise());
            encoder.writeData(ctx, pushPromiseStreamId, payload, 0, true, ctx.newPromise());
        }
    }

}

/*
 * RequestHandlerFactory.java
 * 
 * Copyright (c) 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * PROPRIETARY/CONFIDENTIAL
 *
 * Use is subject to license terms.
 */
package sks.samples.http2.netty.server.handler;

import io.netty.handler.codec.http2.Http2Connection;
import sks.samples.http2.netty.common.SupportedPath;

import java.util.HashMap;
import java.util.Map;

public class RequestHandlerFactory {

    private static RequestHandlerFactory SINGLETON = new RequestHandlerFactory();

    private Map<String, Class<? extends RequestHandler>> resourceToHandlerMap = new HashMap<>();

    private RequestHandlerFactory() {
        initialize();
    }

    public static RequestHandlerFactory getInstance() {
        return SINGLETON;
    }

    private void initialize() {
        resourceToHandlerMap.put(SupportedPath.TIMER_EVENT, TimerEventRequestHandler.class);
        resourceToHandlerMap.put(SupportedPath.PUSH_PROMISE, TimerEventAsPushPromiseRequestHandler.class);
    }

    public RequestHandler getRequestHandler(String requestedPath, Http2Connection connection) {

        RequestHandler handler;
        Class<? extends RequestHandler> result = resourceToHandlerMap.get(requestedPath);
        if (result == null) {
            handler = new UnknownRequestHandler();
        } else {
            try {
                handler = result.newInstance();
            } catch (InstantiationException | IllegalAccessException iae) {
                //this shouldn't happen
                iae.printStackTrace();
                throw new IllegalStateException("Unable to load resource", iae);
            }
        }

        handler.setConnection(connection);
        return handler;
    }
}

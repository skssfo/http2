
package sks.samples.http2.netty.server.handler;

import io.netty.handler.codec.http2.Http2Connection;

public abstract class AbstractRequestHandlerBase implements RequestHandler {

    private Http2Connection connection;

    @Override
    public void setConnection(Http2Connection connection) {
        this.connection = connection;
    }

    @Override
    public Http2Connection getConnection() {
        return connection;
    }
}

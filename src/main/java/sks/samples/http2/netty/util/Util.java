
package sks.samples.http2.netty.util;

import io.netty.handler.codec.http2.Http2SecurityUtil;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.SupportedCipherSuiteFilter;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public final class Util {

    private Util() {
    }

    public static SslContext getSslContext(boolean server) throws SSLException, CertificateException {

        SelfSignedCertificate ssc = new SelfSignedCertificate();
        SslContextBuilder builder = null;

        if (server) {
            builder = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey());
        } else {
            builder = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE);
        }

        SslContext sslCtx = builder
                .sslProvider(SslProvider.JDK)
                .ciphers(Http2SecurityUtil.CIPHERS, SupportedCipherSuiteFilter.INSTANCE)
                .applicationProtocolConfig(new ApplicationProtocolConfig(
                        ApplicationProtocolConfig.Protocol.ALPN,
                        ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                        ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT,
                        ApplicationProtocolNames.HTTP_2,
                        ApplicationProtocolNames.HTTP_1_1))
                .build();

        return sslCtx;
    }
}

# http2

## HTTP2 samples using Netty 4.1.

The project has a server and a client target. The client sends a GET request for which the server sends push_promise
request & response every 5 seconds. There is also a code path where the server sends a timer event every 5 seconds
to the client in the same stream that the client sent its request.

The build.gradle file has targets to run the server and the client. Note that the ALPN library needs to be in the boot classpath.

package com.monetary.transfer;

import com.monetary.transfer.config.ApplicationConfig;
import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

class Application {

    public static void main(String[] args) {
        /*
         * Configuring and starting the server
         */
        URI uri = URI.create("http://localhost:8080/");
        ResourceConfig config = new ApplicationConfig();
        final Channel server = NettyHttpContainerProvider.createHttp2Server(uri, config, null);
        /*
         * Setting up shutdown hook
         */
        Thread shutdownHook = new Thread(server::close);
        Runtime .getRuntime()
                .addShutdownHook(shutdownHook);
    }
}

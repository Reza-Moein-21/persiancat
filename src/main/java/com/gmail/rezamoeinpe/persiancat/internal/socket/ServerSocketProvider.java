package com.gmail.rezamoeinpe.persiancat.internal.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocketProvider implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(ServerSocketProvider.class);
    private final ServerSocketChannel serverSocket;
    private final ExecutorService executor;

    public ServerSocketProvider(SocketHandler handler) {
        this(ServerConfig.DEFAULT, handler);
    }

    public ServerSocketProvider(ServerConfig serverConfig, SocketHandler handler) {
        log.debug("Server configuration set to -> {}", serverConfig);

        executor = Executors.newCachedThreadPool();
        log.debug("Executor service is setup");

        try {
            this.serverSocket = ServerSocketChannel.open();
            log.debug("Server socket channel initialized");
            this.serverSocket.bind(new InetSocketAddress(serverConfig.port()));
            log.info("Server ready on port {}", serverConfig.port());

            while (this.serverSocket.isOpen()) {
                final var clientSocket = this.serverSocket.accept();
                log.debug("Client socket accepted {}", clientSocket);
                executor.execute(() -> handelClientSocket(clientSocket, handler));
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void handelClientSocket(SocketChannel clientSocket, SocketHandler handler) {
        try {
            var bytes = handler.handelClientSocket(clientSocket);


            OutputStream outputStream = Channels.newOutputStream(clientSocket);
            PrintWriter w = new PrintWriter(outputStream);
            outputStream.write(bytes);
            w.flush();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {

            if (!this.executor.isShutdown())
                this.executor.shutdown();

            if (this.serverSocket.isOpen())
                this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

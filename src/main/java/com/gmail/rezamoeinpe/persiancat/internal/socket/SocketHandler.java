package com.gmail.rezamoeinpe.persiancat.internal.socket;

import java.nio.channels.SocketChannel;

@FunctionalInterface
public interface SocketHandler {
    byte[] handelClientSocket(SocketChannel clientSocket);
}

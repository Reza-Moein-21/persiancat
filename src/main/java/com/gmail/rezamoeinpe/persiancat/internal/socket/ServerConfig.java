package com.gmail.rezamoeinpe.persiancat.internal.socket;

public record ServerConfig(int port, String ip) {
    public static final ServerConfig DEFAULT = new ServerConfig(8080, "");
}

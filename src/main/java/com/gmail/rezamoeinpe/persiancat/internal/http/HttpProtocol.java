package com.gmail.rezamoeinpe.persiancat.internal.http;

import java.util.Arrays;
import java.util.Optional;

public enum HttpProtocol {
    HTTP_1_1("HTTP/1.1", "1.1");

    private final String title;
    private final String version;

    HttpProtocol(String title, String version) {
        this.title = title;
        this.version = version;
    }

    public static Optional<HttpProtocol> findByTitle(String title) {
        return Arrays.stream(values()).filter(e -> e.getTitle().equals(title)).findFirst();
    }

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return this.title;
    }
}

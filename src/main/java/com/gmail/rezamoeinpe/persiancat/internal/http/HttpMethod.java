package com.gmail.rezamoeinpe.persiancat.internal.http;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;
    public static final int MAX_SIZE = Arrays.stream(values())
            .mapToInt(v -> v.toString().length())
            .max()
            .orElse(0);
}

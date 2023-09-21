package com.gmail.rezamoeinpe.persiancat.internal.http;

import java.util.Set;

public record HttpRequest(HttpMethod method, String path, HttpProtocol protocol, Set<HttpHeader> headers) {
}
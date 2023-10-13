package com.gmail.rezamoeinpe.persiancat.internal.http;

import java.net.URI;
import java.util.List;
import java.util.Set;

public record HttpRequest(HttpMethod method, URI uri, HttpProtocol protocol, Set<HttpHeader> headers,
                          List<String> body) {
}

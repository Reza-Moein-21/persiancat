package com.gmail.rezamoeinpe.persiancat.test.http.request;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpProtocol;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpStatus;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParser;
import com.gmail.rezamoeinpe.persiancat.test.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.channels.ReadableByteChannel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class HttpRequestParserLine1Test {

    public static final ReadableByteChannel VALID_HTTP_REQUEST = TestUtils.toByteChannel("GET / HTTP/1.1\r\n" +
            "Host: 127.0.0.1:8080\r\n" +
            "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:124.0) Gecko/20100101 Firefox/124.0\r\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
            "Accept-Language: en-US,en;q=0.5\r\n" +
            "Accept-Encoding: gzip, deflate, br\r\n" +
            "Connection: keep-alive\r\n" +
            "Upgrade-Insecure-Requests: 1\r\n" +
            "Sec-Fetch-Dest: document\r\n" +
            "Sec-Fetch-Mode: navigate\r\n" +
            "Sec-Fetch-Site: none\r\n" +
            "Sec-Fetch-User: ?1\r\n" +
            "\r\n");

    public static final ReadableByteChannel BAD_METHOD = TestUtils.toByteChannel(
            "GEt / HTTP/1.1\r\n" +
                    "\r\n");
    public static final ReadableByteChannel BAD_METHOD_TO_LONG = TestUtils.toByteChannel(
            "GETTTTTTTTTTTTT / HTTP/1.1\r\n" +
                    "\r\n");

    public static final ReadableByteChannel EXTRA_ITEM_REQUEST_LINE = TestUtils.toByteChannel(
            "GET /  AAA HTTP/1.1\r\n" +
                    "\r\n");

    public static final ReadableByteChannel EMPTY_REQUEST_LINE = TestUtils.toByteChannel(
            "\r\n" +
                    "Host: 127.0.0.1:8080\r\n" +
                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:124.0) Gecko/20100101 Firefox/124.0\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                    "Accept-Language: en-US,en;q=0.5\r\n" +
                    "Accept-Encoding: gzip, deflate, br\r\n" +
                    "\r\n");

    public static final ReadableByteChannel REQUEST_LINE_ONLY_CR = TestUtils.toByteChannel(
            "GET / HTTP/1.1\r" +  // No line feed
                    "Host: 127.0.0.1:8080\r\n" +
                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:124.0) Gecko/20100101 Firefox/124.0\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                    "Accept-Language: en-US,en;q=0.5\r\n" +
                    "Accept-Encoding: gzip, deflate, br\r\n" +
                    "\r\n");

    public static final ReadableByteChannel REQUEST_LINE_NO_CR_LF = TestUtils.toByteChannel(
            "GET / HTTP/1.1" +  // No CR LF
                    "Host: 127.0.0.1:8080\r\n" +
                    "User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:124.0) Gecko/20100101 Firefox/124.0\r\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                    "Accept-Language: en-US,en;q=0.5\r\n" +
                    "Accept-Encoding: gzip, deflate, br\r\n" +
                    "\r\n");

    HttpRequestParser parser;

    @BeforeEach
    void setUp() {
        parser = new HttpRequestParser();
    }

    @Test
    void validRequest_pars_methodShouldBeGet() {
        var req = parser.pars(VALID_HTTP_REQUEST);
        assertThat(req.method()).isEqualTo(HttpMethod.GET);
        assertThat(req.uri().getPath()).isEqualTo("/");
        assertThat(req.protocol()).isEqualTo(HttpProtocol.HTTP_1_1);
    }

    @Test
    void badHttpMethodRequest_pars_ShouldThrowNotImplemented() {
        var except = catchThrowableOfType(() -> parser.pars(BAD_METHOD), HttpRequestParserException.class);
        assertThat(except.getCode()).isEqualTo(String.valueOf(HttpStatus.NOT_IMPLEMENTED.getCode()));
        assertThat(except.getAddition()).containsAnyOf("Method GEt not implemented");

    }

    @Test
    void badHttpMethodRequest_pars_ShouldThrowNotImplemented2() {
        var except = catchThrowableOfType(() -> parser.pars(BAD_METHOD_TO_LONG), HttpRequestParserException.class);
        assertThat(except.getCode()).isEqualTo(String.valueOf(HttpStatus.NOT_IMPLEMENTED.getCode()));
        assertThat(except.getAddition()).containsAnyOf("Method length is too long");
    }

    @Test
    void extraItemInRequestLine_pars_ShouldThrowBadRequest() {
        var req = catchThrowableOfType(() -> parser.pars(EXTRA_ITEM_REQUEST_LINE), HttpRequestParserException.class);
        assertThat(req.getCode()).isEqualTo(String.valueOf(HttpStatus.BAD_REQUEST.getCode()));
    }

    @Test
    void emptyRequestLine_pars_ShouldThrowBadRequest() {
        var req = catchThrowableOfType(() -> parser.pars(EMPTY_REQUEST_LINE), HttpRequestParserException.class);
        assertThat(req.getCode()).isEqualTo(String.valueOf(HttpStatus.BAD_REQUEST.getCode()));
    }

    @Test
    void requestLineOnlyCRNotLineFeed_pars_ShouldThrowBadRequest() {
        var req = catchThrowableOfType(() -> parser.pars(REQUEST_LINE_ONLY_CR), HttpRequestParserException.class);
        assertThat(req.getCode()).isEqualTo(String.valueOf(HttpStatus.BAD_REQUEST.getCode()));
    }

    @Test
    void requestLineNoCrLF_pars_ShouldThrowBadRequest() {
        var req = catchThrowableOfType(() -> parser.pars(REQUEST_LINE_NO_CR_LF), HttpRequestParserException.class);
        assertThat(req.getCode()).isEqualTo(String.valueOf(HttpStatus.HTTP_VERSION_NOT_SUPPORTED.getCode()));
        assertThat(req.getAddition()).isEqualTo("Http version is not valid");
    }
}

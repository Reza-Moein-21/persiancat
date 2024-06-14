package com.gmail.rezamoeinpe.persiancat.internal.http.parser;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.internal.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.*;


public class HttpRequestParser {
    public static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestParser.class);
    private static final byte SP = 0x20;
    private static final byte CR = 0x0D;
    private static final byte LF = 0x0A;

    private static final int BUFFER_SIZE = 1024;
    private static final int MAX_URI_SIZE = 30; // TODO should find the right number

    private HttpMethod method;
    private URI uri;
    private HttpProtocol protocol;


    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private ReadableByteChannel byteChannel;

    private final Set<HttpHeader> headers = new HashSet<>();
    private final List<String> body = new ArrayList<>();
    private boolean headerBufferCleared = false;


    public HttpRequest pars(ReadableByteChannel byteChannel) throws HttpRequestParserException {
        final StringBuilder processBuffer = new StringBuilder();

        if (Objects.isNull(byteChannel)) throw HttpRequestParserException.NULL_REQUEST;

        this.byteChannel = byteChannel;

        byte aByte;

        while ((aByte = readAByte()) > 0) {
            LOGGER.debug("{}={}", aByte, (char) aByte);
            if (!isLineOneProcessed()) {
                processLineOne(aByte, processBuffer);
                continue;
            }

            processHeaders(aByte, processBuffer);
        }

        if (aByte == 0 && this.method == null) throw HttpRequestParserException.EMPTY_REQUEST;

        return new HttpRequest(this.method, this.uri, this.protocol, this.headers, this.body);
    }

    private boolean isLineOneProcessed() {
        return this.protocol != null;
    }

    private void processHeaders(byte aByte, StringBuilder processBuffer) {
        if (!headerBufferCleared) {
            clearProcessBuffer(processBuffer);
            headerBufferCleared = true;
        }
        if (aByte == CR) {
            var nextByte = readAByte();
            if (nextByte == LF) {
                LOGGER.debug("Header: {}", processBuffer);
                var headerText = processBuffer.toString();
                if (headerText.isEmpty()) return;

                var headerKeyValue = headerText.split(":");

                if (headerKeyValue.length < 2)
                    throw HttpRequestParserException.INVALID_HEADER_FORMAT;

                headers.add(new HttpHeader(headerKeyValue[0], headerKeyValue[1]));
                clearProcessBuffer(processBuffer);
                return;
            } else {
                throw HttpRequestParserException.ofHttpStatus(HttpStatus.BAD_REQUEST);
            }
        }
        processBuffer.append((char) aByte);

    }

    private byte readAByte() {

        if (!buffer.hasRemaining()) return 0;

        byte aByte = buffer.get();

        if (aByte == 0) {
            buffer.rewind();
            try {
                this.byteChannel.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            buffer.flip();
            try {
                aByte = buffer.get();
            } catch (Exception e) {
                return 0;
            }
        }
        return aByte;
    }

    private void processLineOne(byte aByte, StringBuilder processBuffer) {

        if (aByte == CR) {
            var nextByte = readAByte();
            if (nextByte == LF) {
                LOGGER.debug("Request-line Version: {}", processBuffer);
                if (this.method == null || this.uri == null)
                    throw HttpRequestParserException.ofHttpStatus(HttpStatus.BAD_REQUEST);
                this.protocol = HttpProtocol.findByTitle(processBuffer.toString()).orElseThrow(() -> HttpRequestParserException.INVALID_HTTP_VERSION);
                return;
            } else {
                throw HttpRequestParserException.ofHttpStatus(HttpStatus.BAD_REQUEST);
            }
        }


        if (aByte == SP) {
            if (this.method == null) {
                LOGGER.debug("Request-line method: {}", processBuffer);
                try {
                    this.method = HttpMethod.valueOf(processBuffer.toString());
                } catch (IllegalArgumentException e) {
                    throw HttpRequestParserException.ofHttpStatus(HttpStatus.NOT_IMPLEMENTED, String.format("Method %s not implemented", processBuffer));
                }
            } else if (this.uri == null) {
                LOGGER.debug("Request-line uri: {}", processBuffer);
                try {
                    this.uri = new URI(processBuffer.toString());
                } catch (URISyntaxException e) {
                    throw HttpRequestParserException.INVALID_REQUEST_PATH;
                }
            } else {
                throw HttpRequestParserException.ofHttpStatus(HttpStatus.BAD_REQUEST);
            }

            clearProcessBuffer(processBuffer);
        } else {
            processBuffer.append((char) aByte);

            if (this.method == null && processBuffer.length() > HttpMethod.MAX_SIZE)
                throw HttpRequestParserException.ofHttpStatus(HttpStatus.NOT_IMPLEMENTED, "Method length is too long");

            if (this.method != null && this.uri == null && processBuffer.length() > MAX_URI_SIZE)
                throw HttpRequestParserException.ofHttpStatus(HttpStatus.REQUEST_URI_TOO_LARGE);

            if (this.method != null && this.uri != null && this.protocol == null && processBuffer.length() > HttpProtocol.MAX_SIZE)
                throw HttpRequestParserException.INVALID_HTTP_VERSION;
        }
    }

    private void clearProcessBuffer(StringBuilder processBuffer) {
        processBuffer.delete(0, processBuffer.length());
    }
}

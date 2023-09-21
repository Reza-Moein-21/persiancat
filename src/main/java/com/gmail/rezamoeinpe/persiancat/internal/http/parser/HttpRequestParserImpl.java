package com.gmail.rezamoeinpe.persiancat.internal.http.parser;

import com.gmail.rezamoeinpe.persiancat.internal.http.HttpHeader;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpProtocol;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpRequest;
import com.gmail.rezamoeinpe.persiancat.internal.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class HttpRequestParserImpl implements HttpRequestParser {
    public static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestParserImpl.class);

    private HttpMethod method;
    private String path;
    private HttpProtocol protocol;

    private Set<HttpHeader> headers = new HashSet<>();
    private List<String> body = new ArrayList<>();

    @Override
    public HttpRequest pars(InputStream inputStream) {
        Objects.requireNonNull(inputStream);
        var reader = new BufferedReader(new InputStreamReader(inputStream));

        var line = "";
        var emptyLineDetected = false;
        try {

            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;

                LOGGER.trace("Reading lineNo {}", lineNo);

                if (lineNo == 1) {
                    processLineOne(line);
                    continue;
                }

                if (isEmptyLine(line)) {
                    emptyLineDetected = true;
                    continue;
                }

                if (emptyLineDetected)
                    processBody(line);
                else
                    processHeader(line);
            }

        } catch (IOException e) {
        }

        return new HttpRequest(this.method, this.path, this.protocol, this.headers);
    }

    private void processHeader(String line) {
        var headerNameValue = line.split(":");
        final var header = new HttpHeader(StringUtils.trim(headerNameValue[0]), StringUtils.trim(headerNameValue[1]));
        this.headers.add(header);
    }

    private void processBody(String line) {
        this.body.add(line);
    }

    private boolean isEmptyLine(String line) {
        return line.isEmpty() || line.equals("\r\n");
    }

    private void processLineOne(String line1) {
        var lines = line1.split(" ");
        if (lines.length < 2) {
            parserExceptionThrower(1, "Invalid Http");
        }

        var l1 = lines[0];
        try {
            this.method = HttpMethod.valueOf(l1);
        } catch (IllegalArgumentException e) {
            parserExceptionThrower(1, "Invalid HttpMethod" + " => " + l1);
        }

        this.path = lines[1];
        this.protocol = HttpProtocol.findByTitle(lines[2]).orElseThrow();
    }

    private void parserExceptionThrower(int lineNumber, String message) {
        throw new IllegalArgumentException("Error in line: " + lineNumber + ": " + message);
    }
}
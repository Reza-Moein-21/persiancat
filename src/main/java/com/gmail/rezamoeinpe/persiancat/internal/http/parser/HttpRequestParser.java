package com.gmail.rezamoeinpe.persiancat.internal.http.parser;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyCause;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


public class HttpRequestParser {
    public static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestParser.class);

    private HttpMethod method;
    private URI uri;
    private HttpProtocol protocol;

    private final Set<HttpHeader> headers = new HashSet<>();
    private final List<String> body = new ArrayList<>();

    public HttpRequest pars(InputStream inputStream) throws HttpRequestParserException {
        if (Objects.isNull(inputStream))
            throw new HttpRequestParserException(new HttpRequestParserException.NullRequest());

        var reader = new BufferedReader(new InputStreamReader(inputStream));

        var line = "";
        var emptyLineDetected = false;

        int lineNo = 0;
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new HttpRequestParserException(new CattyCause.GeneralCause(e));
            }
            lineNo++;

            LOGGER.trace("Reading lineNo {}", lineNo);

            if (lineNo == 1)
                processLineOne(line);
            else if (isEmptyLine(line))
                emptyLineDetected = true;
            else if (emptyLineDetected)
                processBody(line);
            else
                processHeader(line);
        }

        if (lineNo == 0)
            throw new HttpRequestParserException(new HttpRequestParserException.EmptyRequest());


        return new HttpRequest(this.method, this.uri, this.protocol, this.headers, this.body);
    }

    private void processHeader(String line) {
        var firstColonIndex = line.indexOf(":");

        if (firstColonIndex == -1)
            throw new HttpRequestParserException(new HttpRequestParserException.InvalidHeaderFormat());

        var key = line.substring(0, firstColonIndex);
        var value = line.substring(firstColonIndex + 1);

        final var header = new HttpHeader(StringUtils.trim(key), StringUtils.trim(value));
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
        if (lines.length == 0)
            throw new HttpRequestParserException(new HttpRequestParserException.EmptyRequest());

        var l1 = lines[0];
        try {
            this.method = HttpMethod.valueOf(l1);
        } catch (IllegalArgumentException e) {
            throw new HttpRequestParserException(new HttpRequestParserException.InvalidHttpMethod());
        }

        if (lines.length == 1)
            throw new HttpRequestParserException(new HttpRequestParserException.EmptyRequestPath());

        try {
            this.uri = new URI(lines[1]);
        } catch (URISyntaxException e) {
            throw new HttpRequestParserException(new HttpRequestParserException.InvalidRequestPath());
        }

        if (lines.length == 2)
            throw new HttpRequestParserException(new HttpRequestParserException.EmptyHTTPVersion());


        this.protocol = HttpProtocol.findByTitle(lines[2])
                .orElseThrow(() -> new HttpRequestParserException(new HttpRequestParserException.InvalidHTTPVersion()));
    }
}

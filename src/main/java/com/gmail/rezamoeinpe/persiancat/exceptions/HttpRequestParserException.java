package com.gmail.rezamoeinpe.persiancat.exceptions;

import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyCause;
import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyException;

public final class HttpRequestParserException extends CattyException {
    public HttpRequestParserException(CattyCause cause) {
        super(cause);
    }

    public static class NullRequest extends CattyCause {
        public NullRequest() {
            super("HTTP request should not be null");
        }
    }

    public static class EmptyRequest extends CattyCause {
        public EmptyRequest() {
            super("HTTP request should not be empty string");
        }
    }

    public static class InvalidHttpMethod extends CattyCause {
        public InvalidHttpMethod() {
            super("Http method not supported");
        }
    }

    public static class EmptyRequestPath extends CattyCause {
        public EmptyRequestPath() {
            super("Http request uri required");
        }
    }

    public static class InvalidRequestPath extends CattyCause {
        public InvalidRequestPath() {
            super("Http request uri is not valid");
        }
    }

    public static class EmptyHTTPVersion extends CattyCause {
        public EmptyHTTPVersion() {
            super("Http request version required");
        }
    }

    public static class InvalidHTTPVersion extends CattyCause {
        public InvalidHTTPVersion() {
            super("Http version is not valid");
        }
    }

    public static class InvalidHeaderFormat extends CattyCause {
        public InvalidHeaderFormat() {
            super("Http header format is not valid");
        }
    }
}

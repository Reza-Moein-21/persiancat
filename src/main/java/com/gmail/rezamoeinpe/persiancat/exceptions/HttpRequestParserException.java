package com.gmail.rezamoeinpe.persiancat.exceptions;

import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyCause;
import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyException;

public final class HttpRequestParserException extends CattyException {
    public HttpRequestParserException(CattyCause cause) {
        super(cause);
    }

    public static class RequestInputStreamRequired extends CattyCause {
        public RequestInputStreamRequired() {
            super("Input stream required to pars http request");
        }
    }

    public static class InvalidHttpMethod extends CattyCause {
        public InvalidHttpMethod() {
            super("Http method not valid");
        }
    }

}

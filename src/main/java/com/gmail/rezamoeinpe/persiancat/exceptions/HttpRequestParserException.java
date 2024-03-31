package com.gmail.rezamoeinpe.persiancat.exceptions;

import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyException;
import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyExceptionInfo;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpStatus;

public final class HttpRequestParserException extends CattyException {
    public static final HttpRequestParserException NULL_REQUEST = HttpRequestParserException.ofHttpStatus(HttpStatus.BAD_REQUEST, "HTTP request should not be null");
    public static final HttpRequestParserException EMPTY_REQUEST = HttpRequestParserException.ofHttpStatus(HttpStatus.BAD_REQUEST, "HTTP request is empty");
    public static final HttpRequestParserException INVALID_REQUEST_PATH = HttpRequestParserException.ofHttpStatus(HttpStatus.BAD_REQUEST, "Http request uri is not valid");
    public static final HttpRequestParserException INVALID_HTTP_VERSION = HttpRequestParserException.ofHttpStatus(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "Http version is not valid");

    private HttpRequestParserException(CattyExceptionInfo error) {
        super(error);
    }

    public static HttpRequestParserException ofHttpStatus(HttpStatus status) {
        return new HttpRequestParserException(new CattyExceptionInfo(String.valueOf(status.getCode()), status.getDescription()));
    }

    public static HttpRequestParserException ofHttpStatus(HttpStatus status, String addition) {
        return new HttpRequestParserException(new CattyExceptionInfo(String.valueOf(status.getCode()), status.getDescription(), addition));
    }

}

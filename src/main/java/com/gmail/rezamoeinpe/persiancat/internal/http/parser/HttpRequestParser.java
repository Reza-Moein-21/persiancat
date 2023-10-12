package com.gmail.rezamoeinpe.persiancat.internal.http.parser;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpRequest;

import java.io.InputStream;

public interface HttpRequestParser {
    HttpRequest pars(InputStream inputStream) throws HttpRequestParserException;
}

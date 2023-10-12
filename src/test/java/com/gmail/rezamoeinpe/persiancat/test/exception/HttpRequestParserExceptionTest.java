package com.gmail.rezamoeinpe.persiancat.test.exception;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestParserExceptionTest {
    @Test
    void throwingHttpRequestParserException_throwWithInvalidHttpMethodCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.InvalidHttpMethod());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.InvalidHttpMethod.class)
                .hasToString("HttpRequestParserException [Http method not valid]");
    }
    @Test
    void throwingHttpRequestParserException_throwWithRequestInputStreamRequiredCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.RequestInputStreamRequired());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.RequestInputStreamRequired.class)
                .hasToString("HttpRequestParserException [Input stream required to pars http request]");
    }
}

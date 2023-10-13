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
                .hasToString("HttpRequestParserException [Http method not supported]");
    }

    @Test
    void throwingHttpRequestParserException_throwWithNullRequestCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.NullRequest());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.NullRequest.class)
                .hasToString("HttpRequestParserException [HTTP request should not be null]");
    }

    @Test
    void throwingHttpRequestParserException_throwWithEmptyRequestCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.EmptyRequest());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.EmptyRequest.class)
                .hasToString("HttpRequestParserException [HTTP request should not be empty string]");
    }

    @Test
    void throwingHttpRequestParserException_throwWithEmptyRequestPathCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.EmptyRequestPath());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.EmptyRequestPath.class)
                .hasToString("HttpRequestParserException [Http request uri required]");
    }

    @Test
    void throwingHttpRequestParserException_throwWithInvalidRequestPathCause_shouldGetExpectedToString() {
        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.InvalidRequestPath());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.InvalidRequestPath.class)
                .hasToString("HttpRequestParserException [Http request uri is not valid]");
    }

    @Test
    void throwingHttpRequestParserException_throwWithEmptyHTTPVersionCause_shouldGetExpectedToString() {
        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.EmptyHTTPVersion());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.EmptyHTTPVersion.class)
                .hasToString("HttpRequestParserException [Http request version required]");
    }

    @Test
    void throwingHttpRequestParserException_throwWithInvalidHTTPVersionCause_shouldGetExpectedToString() {
        assertThatThrownBy(() -> {
            throw new HttpRequestParserException(new HttpRequestParserException.InvalidHTTPVersion());
        }, "should throw exception")
                .hasCauseInstanceOf(HttpRequestParserException.InvalidHTTPVersion.class)
                .hasToString("HttpRequestParserException [Http version is not valid]");
    }
}

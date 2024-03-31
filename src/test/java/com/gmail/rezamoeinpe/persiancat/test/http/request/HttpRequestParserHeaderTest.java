package com.gmail.rezamoeinpe.persiancat.test.http.request;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpHeader;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParser;
import com.gmail.rezamoeinpe.persiancat.test.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// TODO after implementing header parser, will be activated
@Disabled
class HttpRequestParserHeaderTest {

    HttpRequestParser p;

    @BeforeEach
    void setUp() {
        p = new HttpRequestParser();
    }

    @Test
    void givingNoHeaderRequest_pars_shouldGetEmptyHeaders() {
        var noHeaderExample1 = TestUtils.toByteChannel("""
                POST /mock?q=mock HTTP/1.1
                """);
        var result = p.pars(noHeaderExample1);
        assertThat(result.headers()).isEmpty();
        var noHeaderExample2 = TestUtils.toByteChannel("""
                POST /mock?q=mock HTTP/1.1
                                
                                
                body
                """);
        result = p.pars(noHeaderExample2);
        assertThat(result.headers()).isEmpty();
    }

    @Test
    void givingWrongFormatHeader_pars_shouldThrow() {
        var wrongHeader = TestUtils.toByteChannel("""
                POST /mock?q=mock HTTP/1.1
                worg-header wrong-value
                """);

        assertThatThrownBy(() -> p.pars(wrongHeader))
                .isInstanceOf(HttpRequestParserException.class);
//                .hasCauseInstanceOf(HttpRequestParserException.InvalidHeaderFormat.class);

        var wrongHeader2 = TestUtils.toByteChannel("""
                POST /mock?q=mock HTTP/1.1
                Connection: Keep-Alive
                worg-header wrong-value
                """);

        assertThatThrownBy(() -> p.pars(wrongHeader2))
                .isInstanceOf(HttpRequestParserException.class);
//                .hasCauseInstanceOf(HttpRequestParserException.InvalidHeaderFormat.class);
    }

    @Test
    void givingValidHeaders_pars_shouldGetExpectedHeaders() {
        var validHeader = TestUtils.toByteChannel("""
                POST /mock?q=mock HTTP/1.1
                Connection: Keep-Alive
                Authorization: Basic !39ssg340+;lsf#$7sf,
                server: Google Frontend
                content-encoding: br
                date: Fri, 13 Oct 2023 18:30:27 GMT
                expires: Fri, 13 Oct 2023 19:23:19 GMT
                cache-control: public, max-age=3600
                age: 2705
                content-type: text/javascript
                content-length: 7
                x-cache: hit
                                
                content
                """);

        var result = p.pars(validHeader);
        assertThat(result.headers())
                .containsAll(
                        Set.of(
                                new HttpHeader("Connection", "Keep-Alive"),
                                new HttpHeader("Authorization", "Basic !39ssg340+;lsf#$7sf,"),
                                new HttpHeader("server", "Google Frontend"),
                                new HttpHeader("content-encoding", "br"),
                                new HttpHeader("date", "Fri, 13 Oct 2023 18:30:27 GMT"),
                                new HttpHeader("expires", "Fri, 13 Oct 2023 19:23:19 GMT"),
                                new HttpHeader("cache-control", "public, max-age=3600"),
                                new HttpHeader("age", "2705"),
                                new HttpHeader("content-type", "text/javascript"),
                                new HttpHeader("content-length", "7"),
                                new HttpHeader("x-cache", "hit")
                        )
                );
    }
}
package com.gmail.rezamoeinpe.persiancat.test.http.request;

import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParser;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestParserTest {

    private static final String SAMPLE_REQUEST = """
            GET /welcome HTTP/1.1
            Content-Type: application/json
            Host: www.thunderclient.com
            Content-Length: 41
                        
            {
              "name": "reza",
              "family": "moein"
            }
            """;
    HttpRequestParser p;

    @BeforeEach
    void setUp() {
        p = new HttpRequestParserImpl();
    }

    @Test
    void givingNullInputStream_pars_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> p.pars(null), "Should thrown NullPointerException")
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void name() throws IOException {
        var r = new ByteArrayInputStream(SAMPLE_REQUEST.getBytes(StandardCharsets.UTF_8));
        var result = p.pars(r);
        System.out.println(result);
        assertThat(result).isNotNull();
        assertThat(result.method()).isEqualTo(HttpMethod.GET);

    }

}
package com.gmail.rezamoeinpe.persiancat.test.http.request;

import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.internal.http.exception.InvalidHttpMethodException;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParser;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gmail.rezamoeinpe.persiancat.test.util.TestUtils.toInputStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestParserLine1Test {

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
    void givingEmpty_pars_shouldThrownException() {
        var emptyStream = toInputStream("");
        assertThatThrownBy(() -> p.pars(emptyStream))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givingValidLine1ForGETMethod_pars_shouldGetExpectedHttpRequestGETMethod() {
        var sampleRequest = "GET /welcome HTTP/1.1";
        var result = p.pars(toInputStream(sampleRequest));
        assertThat(result)
                .isNotNull()
                .matches(r -> r.method().equals(HttpMethod.GET));
    }

    @Test
    void givingUnsupportedMethod_pars_shouldThrownException() {
        var invalidMethodRequest = toInputStream("SET /hello?name=reza HTTP/1.1");
        assertThatThrownBy(() -> p.pars(invalidMethodRequest))
                .isInstanceOf(InvalidHttpMethodException.class);
    }


}
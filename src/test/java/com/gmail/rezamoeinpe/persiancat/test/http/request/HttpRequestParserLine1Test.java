package com.gmail.rezamoeinpe.persiancat.test.http.request;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpProtocol;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParser;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

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
    void givingNullInputStream_pars_shouldThrowNullRequestException() {
        assertThatThrownBy(() -> p.pars(null), "Should thrown NullRequest")
                .isInstanceOf(HttpRequestParserException.class)
                .hasCauseInstanceOf(HttpRequestParserException.NullRequest.class);
    }

    @Test
    void givingEmpty_pars_shouldThrownEmptyRequestException() {
        var emptyStream = toInputStream("");
        assertThatThrownBy(() -> p.pars(emptyStream))
                .isInstanceOf(HttpRequestParserException.class)
                .hasCauseInstanceOf(HttpRequestParserException.EmptyRequest.class);

        var invalidStream = toInputStream("   ");
        assertThatThrownBy(() -> p.pars(invalidStream))
                .isInstanceOf(HttpRequestParserException.class)
                .hasCauseInstanceOf(HttpRequestParserException.EmptyRequest.class);
    }


    @Nested
    class Line1PathTest {
        @Test
        void givingEmptyURI_pars_shouldThrownException() {
            var noURI = toInputStream("POST");
            assertThatThrownBy(() -> p.pars(noURI))
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.EmptyRequestPath.class);

            var emptyURI = toInputStream("POST      ");
            assertThatThrownBy(() -> p.pars(emptyURI))
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.EmptyRequestPath.class);
        }

        @Test
        void givingInvalidURI_pars_shouldThrownException() {
            var invalidURI = toInputStream("POST http: HTTP/1.1");
            assertThatThrownBy(() -> p.pars(invalidURI))
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.InvalidRequestPath.class);

            var anotherInvalidURI = toInputStream("POST \\ HTTP/1.1");
            assertThatThrownBy(() -> p.pars(anotherInvalidURI))
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.InvalidRequestPath.class);
        }

        @Test
        void givingValidURI_pars_shouldGetExpectedURI() throws URISyntaxException {
            var validLine1 = toInputStream("POST /hello?name:reza HTTP/1.1");
            var result = p.pars(validLine1);
            assertThat(result.uri()).isEqualTo(new URI("/hello?name:reza"));
        }
    }

    @Nested
    class Line1HTTPVersionTest {
        @Test
        void givingEmptyHttpVersion_pars_shouldThrownException() {
            var emptyVersion = toInputStream("POST / ");
            assertThatThrownBy(() -> p.pars(emptyVersion))
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.EmptyHTTPVersion.class);
        }

        @Test
        void givingInvalidHttpVersion_pars_shouldThrownException() {
            var invalidVersion = toInputStream("POST / HTTP");
            assertThatThrownBy(() -> p.pars(invalidVersion))
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.InvalidHTTPVersion.class);
        }

        @Test
        void givingValidHttpProtocol_pars_shouldGetExpectedHttpProtocolName() {
            var validLine1 = toInputStream("POST /test/mock HTTP/1.1");
            var result = p.pars(validLine1);
            assertThat(result.protocol()).isEqualTo(HttpProtocol.HTTP_1_1);
        }
    }

    @Nested
    class Line1MethodTest {
        @Test
        void givingValidLine1ForGETMethod_pars_shouldGetExpectedHttpRequestGETMethod() {
            var sampleRequest = "GET /welcome HTTP/1.1";
            var result = p.pars(toInputStream(sampleRequest));
            assertThat(result)
                    .isNotNull()
                    .matches(r -> r.method().equals(HttpMethod.GET));
        }

        @Test
        void givingValidLine1ForPOSTMethod_pars_shouldGetExpectedHttpRequestPOSTMethod() {
            var sampleRequest = "POST / HTTP/1.1";
            var result = p.pars(toInputStream(sampleRequest));
            assertThat(result)
                    .isNotNull()
                    .matches(r -> r.method().equals(HttpMethod.POST));
        }

        @Test
        void givingValidLine1ForPUTMethod_pars_shouldGetExpectedHttpRequestPUTMethod() {
            var sampleRequest = "PUT / HTTP/1.1";
            var result = p.pars(toInputStream(sampleRequest));
            assertThat(result)
                    .isNotNull()
                    .matches(r -> r.method().equals(HttpMethod.PUT));
        }

        @Test
        void givingValidLine1ForDELETEMethod_pars_shouldGetExpectedHttpRequestDELETEMethod() {
            var sampleRequest = "DELETE /api/v1/del/123 HTTP/1.1";
            var result = p.pars(toInputStream(sampleRequest));
            assertThat(result)
                    .isNotNull()
                    .matches(r -> r.method().equals(HttpMethod.DELETE));
        }

        @Test
        void givingValidLine1ForPATCHMethod_pars_shouldGetExpectedHttpRequestPATCHMethod() {
            var sampleRequest = "PATCH /api/v1/del/123 HTTP/1.1";
            var result = p.pars(toInputStream(sampleRequest));
            assertThat(result)
                    .isNotNull()
                    .matches(r -> r.method().equals(HttpMethod.PATCH));
        }

        @Test
        void givingInvalidLine1HttpMethod_pars_shouldThrownInvalidHttpMethodException() {
            var sampleRequest = "HEAD / HTTP/1.1";
            InputStream inputStream = toInputStream(sampleRequest);
            assertThatThrownBy(() -> p.pars(inputStream), "Should throw InvalidHttpMethod")
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.InvalidHttpMethod.class);
        }

        @Test
        void givingUnsupportedMethod_pars_shouldThrownException() {
            var invalidMethodRequest = toInputStream("SET /hello?name=reza HTTP/1.1");
            assertThatThrownBy(() -> p.pars(invalidMethodRequest))
                    .isInstanceOf(HttpRequestParserException.class)
                    .hasCauseInstanceOf(HttpRequestParserException.InvalidHttpMethod.class);
        }

    }


}
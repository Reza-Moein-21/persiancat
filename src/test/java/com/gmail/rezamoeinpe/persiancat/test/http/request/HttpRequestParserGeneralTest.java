package com.gmail.rezamoeinpe.persiancat.test.http.request;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParser;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gmail.rezamoeinpe.persiancat.test.util.TestUtils.toInputStream;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestParserGeneralTest {
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

}

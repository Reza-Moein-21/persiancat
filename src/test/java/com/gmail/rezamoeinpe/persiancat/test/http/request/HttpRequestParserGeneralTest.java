package com.gmail.rezamoeinpe.persiancat.test.http.request;

import com.gmail.rezamoeinpe.persiancat.exceptions.HttpRequestParserException;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpStatus;
import com.gmail.rezamoeinpe.persiancat.internal.http.parser.HttpRequestParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gmail.rezamoeinpe.persiancat.test.util.TestUtils.toByteChannel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class HttpRequestParserGeneralTest {
    HttpRequestParser p;

    @BeforeEach
    void setUp() {
        p = new HttpRequestParser();
    }

    @Test
    void givingNullInputStream_pars_shouldThrowNullRequestException() {
        var error = catchThrowableOfType(() -> p.pars(null), HttpRequestParserException.class);
        assertThat(error).hasMessageContaining("HTTP request should not be null");
        assertThat(error.getCode()).isEqualTo(String.valueOf(HttpStatus.BAD_REQUEST.getCode()));

    }

    @Test
    void givingEmpty_pars_shouldThrownEmptyRequestException() {
        var emptyStream = toByteChannel("");

        var error = catchThrowableOfType(() -> p.pars(emptyStream), HttpRequestParserException.class);
        assertThat(error).hasMessageContaining("HTTP request is empty");
        assertThat(error.getCode()).isEqualTo(String.valueOf(HttpStatus.BAD_REQUEST.getCode()));


        var invalidStream = toByteChannel("   ");
        error = catchThrowableOfType(() -> p.pars(invalidStream), HttpRequestParserException.class);
        assertThat(error).hasMessageContaining("HTTP request is empty");
        assertThat(error.getCode()).isEqualTo(String.valueOf(HttpStatus.BAD_REQUEST.getCode()));

    }

}

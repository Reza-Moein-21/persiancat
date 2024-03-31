package com.gmail.rezamoeinpe.persiancat.test.exception;

import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyException;
import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyExceptionInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CattyExceptionTest {

    @Test
    void codeAndReasonShouldBeMatch() {
        var error = catchThrowableOfType(() -> {
            throw new CattyExceptionMock(new CattyExceptionInfo("-1", "Any Error"));
        }, CattyExceptionMock.class);

        assertThat(error.getCode()).isEqualTo("-1");
        assertThat(error.getReason()).isEqualTo("Any Error");
    }

    @Test
    void codeIsRequired() {
        assertThatThrownBy(() -> {
            throw new CattyExceptionMock(new CattyExceptionInfo(null, "Any Error"));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Code is required");
    }

    @Test
    void reasonIsRequired() {
        assertThatThrownBy(() -> {
            throw new CattyExceptionMock(new CattyExceptionInfo("-1", null));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reason is required");
    }

    @Test
    void additionShouldBeMatch() {
        var error = catchThrowableOfType(() -> {
            throw new CattyExceptionMock(new CattyExceptionInfo("-1", "Any Error", "Addition Error"));
        }, CattyExceptionMock.class);

        assertThat(error.getAddition()).isEqualTo("Addition Error");
    }

    @Test
    @DisplayName("Giving only code and reason_Message format should be match whit pattern-> [code:reason]")
    void givingCodeAndReason_messageFormatShouldBeMatch() {
        var error = catchThrowableOfType(() -> {
            throw new CattyExceptionMock(new CattyExceptionInfo("01", "Some error"));
        }, CattyExceptionMock.class);

        assertThat(error.getMessage()).isEqualTo("[01:Some error]");
    }

    @Test
    @DisplayName("Giving code and reason and addition_Message format should be match whit pattern-> [code:reason, addition]")
    void givingCodeAndReasonAndAddition_messageFormatShouldBeMatch() {
        var error = catchThrowableOfType(() -> {
            throw new CattyExceptionMock(new CattyExceptionInfo("01", "Some error", "Some addition"));
        }, CattyExceptionMock.class);

        assertThat(error.getMessage()).isEqualTo("[01:Some error, Some addition]");
    }

    @Test
    @DisplayName("toString should be match whit pattern-> <SimpleClassName> getMassage()")
    void toStringFormatShouldBeMatch() {
        var error = catchThrowableOfType(() -> {
            throw new CattyExceptionMock(new CattyExceptionInfo("01", "Some error", "Some addition"));
        }, CattyExceptionMock.class);

        assertThat(error.toString()).isEqualTo("CattyExceptionMock " + error.getMessage());
    }

    public static class CattyExceptionMock extends CattyException {

        protected CattyExceptionMock(CattyExceptionInfo cause) {
            super(cause);
        }
    }
}

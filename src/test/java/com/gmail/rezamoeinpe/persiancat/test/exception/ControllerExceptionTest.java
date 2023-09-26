package com.gmail.rezamoeinpe.persiancat.test.exception;

import com.gmail.rezamoeinpe.persiancat.exceptions.ControllerException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerExceptionTest {

    @Test
    void givingControllerException_shouldGetExpectedCause() {
        ControllerException.MethodNotFound cause = new ControllerException.MethodNotFound();
        var exp = new ControllerException(cause);
        assertThat(exp.cause()).isEqualTo(cause);
    }

    @Test
    void throwingControllerException_throwWithMethodNotFoundCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new ControllerException(new ControllerException.MethodNotFound());
        }, "should throw exception")
                .hasCauseInstanceOf(ControllerException.MethodNotFound.class)
                .hasToString("ControllerException [Controller has not any public method annotated as rest method]");
    }

    @Test
    void throwingControllerException_throwWithNotRestControllerCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new ControllerException(new ControllerException.NotRestController());
        }, "should throw exception")
                .hasCauseInstanceOf(ControllerException.NotRestController.class)
                .hasToString("ControllerException [Controller should annotated with @RestController]");
    }

    @Test
    void throwingControllerException_throwWithNounUniqueURIMappingCause_shouldGetExpectedToString() {

        assertThatThrownBy(() -> {
            throw new ControllerException(new ControllerException.NounUniqueURIMapping());
        }, "should throw exception")
                .hasCauseInstanceOf(ControllerException.NounUniqueURIMapping.class)
                .hasToString("ControllerException [URL mapping should be unique]");
    }

}

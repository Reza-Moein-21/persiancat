package com.gmail.rezamoeinpe.persiancat.test.controller;

import com.gmail.rezamoeinpe.persiancat.exceptions.InvalidControllerException;
import com.gmail.rezamoeinpe.persiancat.exceptions.MethodNotFound;
import com.gmail.rezamoeinpe.persiancat.exceptions.NotRestController;
import com.gmail.rezamoeinpe.persiancat.exceptions.NounUniqueURIMapping;
import com.gmail.rezamoeinpe.persiancat.internal.controller.ControllerParser;
import com.gmail.rezamoeinpe.persiancat.internal.controller.ControllerParserImpl;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.rest.method.GetMethod;
import com.gmail.rezamoeinpe.persiancat.rest.method.PostMethod;
import com.gmail.rezamoeinpe.persiancat.rest.method.RestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ControllerParserTest {

    ControllerParser controllerChecker;

    @BeforeEach
    void setUp() {
        controllerChecker = new ControllerParserImpl();
    }

    @Test
    void givingEmptyController_parsController_shouldThrowNullPointerException() {
        assertThatThrownBy(() -> controllerChecker.parsController(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("pars action required a non-null Controller");
    }

    @Test
    void givingInvalidNotRestController_parsController_shouldThrownException() {
        class NoRestController {
        }

        var noRestController = new NoRestController();
        assertThatThrownBy(() -> controllerChecker.parsController(noRestController))
                .isInstanceOf(InvalidControllerException.class)
                .hasCauseInstanceOf(NotRestController.class)
                .hasMessageContaining("Controller should annotated with @RestController");
    }

    @Test
    void givingRestControllerNoMethodAnnotation_parsController_shouldThrownException() {
        @RestController
        class NoMethodRestController {
        }

        var noMethodRestController = new NoMethodRestController();
        assertThatThrownBy(() -> controllerChecker.parsController(noMethodRestController))
                .isInstanceOf(InvalidControllerException.class)
                .hasCauseInstanceOf(MethodNotFound.class)
                .hasMessageContaining("Controller has not any public method annotated as rest method");
    }

    @Test
    void givingRestControllerWithMultipleRestMethod_parsController_shouldThrownException() {
        @RestController
        class MockRestController {
            @GetMethod
            @PostMethod
            @SuppressWarnings("unused")
            public void mock() {
            }
        }

        var mockRestController = new MockRestController();
        assertThatThrownBy(() -> controllerChecker.parsController(mockRestController))
                .isInstanceOf(InvalidControllerException.class)
                .hasCauseInstanceOf(NounUniqueURIMapping.class)
                .hasMessageContaining("URL mapping should be unique");
    }

    @Test
    void givingRestControllerWithMultipleRestMethodPath_parsController_shouldThrownException() {
        @RestController
        class MockRestController {
            @GetMethod
            @SuppressWarnings("unused")
            public void mock() {
            }

            @GetMethod("")
            @SuppressWarnings("unused")
            public void mock2() {
            }

            @PostMethod
            @SuppressWarnings("unused")
            public void mock3() {
            }

        }
        var mockRestController = new MockRestController();
        assertThatThrownBy(() -> controllerChecker.parsController(mockRestController))
                .isInstanceOf(InvalidControllerException.class)
                .hasCauseInstanceOf(NounUniqueURIMapping.class)
                .hasMessageContaining("URL mapping should be unique");
    }

    @Test
    void givingValidRestControllerWithOneValidRestMethod_parsController_shouldGetExpectedControllerInfo() {
        @RestController
        class MockValidRestController {
            @PostMethod
            @SuppressWarnings("unused")
            public void mock() {
            }
        }

        var controllerInfo = controllerChecker.parsController(new MockValidRestController());
        assertThat(controllerInfo).isNotNull();
        assertThat(controllerInfo.prefixPath()).isEmpty();
        assertThat(controllerInfo.methods()).hasSize(1);
        assertThat(controllerInfo.methods()).allMatch(m -> m.name().equals(HttpMethod.POST));
        assertThat(controllerInfo.methods()).allMatch(m -> m.path().equals("/"));
    }


    @Test
    void givingValidRestControllerWithThreeValidRestMethod_parsController_shouldGetExpectedControllerInfo() {
        @RestController("/api/mock")
        class MockValidRestController {
            @PostMethod("/save")
            @SuppressWarnings("unused")
            public Object save(Object mockBody) {
                return mockBody;
            }

            @GetMethod
            @SuppressWarnings("unused")
            public List<Object> search(String searchQuery) {
                return List.of(searchQuery);
            }
        }

        MockValidRestController controller = new MockValidRestController();
        var controllerInfo = controllerChecker.parsController(controller);
        assertThat(controllerInfo).isNotNull();
        assertThat(controllerInfo.prefixPath()).isEqualTo("/api/mock");
        assertThat(controllerInfo.methods()).hasSize(2);

        assertThat(controllerInfo.methods()).anyMatch(methodInfo -> methodInfo.path().equals("/save") && methodInfo.name().equals(HttpMethod.POST));
        assertThat(controllerInfo.methods()).anyMatch(methodInfo -> methodInfo.path().equals("/") && methodInfo.name().equals(HttpMethod.GET));

    }
}

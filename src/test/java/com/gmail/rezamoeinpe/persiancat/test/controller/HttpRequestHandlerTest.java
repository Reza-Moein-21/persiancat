package com.gmail.rezamoeinpe.persiancat.test.controller;

import com.gmail.rezamoeinpe.persiancat.internal.controller.ControllerInfo;
import com.gmail.rezamoeinpe.persiancat.internal.http.handler.HttpRequestHandler;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpProtocol;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpRequest;
import com.gmail.rezamoeinpe.persiancat.rest.method.PostMethod;
import com.gmail.rezamoeinpe.persiancat.rest.method.RestController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HttpRequestHandlerTest {
    HttpRequestHandler handler = new HttpRequestHandler();

    @Test
    void name() throws NoSuchMethodException {
        MockController mc = new MockController();

        var methods = new ControllerInfo.MethodInfo(HttpMethod.POST, "/name", mc.getClass().getMethod("save", MyModel.class));
        var ci = new ControllerInfo("", Set.of(methods), mc);


        var request = new HttpRequest(HttpMethod.POST, URI.create("/hello"), HttpProtocol.HTTP_1_1, Set.of(), List.of("{", "id", ":", "123", ",", "name", ":", "Reza", "}"));
        handler.handelRequest(request, Set.of(ci));
        assertThat(mc.testModel).isEqualTo(new MyModel(123L, "Reza"));
        assertThat(mc.saveIsCalled).isTrue();

    }


    @RestController
    public static class MockController {
        public MyModel testModel;
        public boolean saveIsCalled;

        @PostMethod("/name")
        public void save(MyModel myModel) {
            this.testModel = myModel;

            saveIsCalled = true;
        }
    }

    public record MyModel(Long id, String name) {
    }


}
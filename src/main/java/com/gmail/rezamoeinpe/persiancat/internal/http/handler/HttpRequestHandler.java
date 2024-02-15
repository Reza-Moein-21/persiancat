package com.gmail.rezamoeinpe.persiancat.internal.http.handler;

import com.gmail.rezamoeinpe.persiancat.internal.controller.ControllerInfo;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpRequest;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class HttpRequestHandler {

    // TODO not completed
    public void handelRequest(HttpRequest request, Collection<ControllerInfo> controllers) {

        controllers.forEach(ci -> {
            var path = request.uri().getPath();
            if (path.startsWith(ci.prefixPath())) {
                ci.methods().forEach(mi -> {
                    if (mi.name().equals(request.method())) {
                        try {
                            var g = new Gson();
                            String json = String.join("", request.body());
                            Method method = mi.actualMethod();
                            Object jsonObject = g.fromJson(json, method.getParameterTypes()[0]);
                            method.invoke(ci.actualController(), jsonObject);

                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });

    }

}

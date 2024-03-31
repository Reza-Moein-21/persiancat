package com.gmail.rezamoeinpe.persiancat.internal.controller;

import com.gmail.rezamoeinpe.persiancat.exceptions.ControllerException;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;
import com.gmail.rezamoeinpe.persiancat.rest.method.GetMethod;
import com.gmail.rezamoeinpe.persiancat.rest.method.PostMethod;
import com.gmail.rezamoeinpe.persiancat.rest.method.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ControllerParser {
    public static final Logger LOGGER = LoggerFactory.getLogger(ControllerParser.class);

    private static final Set<Class<? extends Annotation>> VALID_REST_METHODS = Set.of(
            GetMethod.class,
            PostMethod.class);

    public ControllerInfo parsController(Object controller) throws ControllerException {
        LOGGER.trace("{}", controller);
        Objects.requireNonNull(controller, "pars action required a non-null Controller");

        Class<?> controllerClass = controller.getClass();
        String pathPrefix = getPathPrefix(controllerClass);

        if (hasNotAnyRestMethod(controllerClass))
            throw ControllerException.METHOD_NOT_FOUND;

        var methods = getAvailableMethods(controllerClass);


        var controllerInfo = new ControllerInfo(pathPrefix, methods, controller);

        if (!areAllPathsUnique(controllerInfo))
            throw ControllerException.NOUN_UNIQUE_URI_MAPPING;


        return controllerInfo;
    }

    private boolean areAllPathsUnique(ControllerInfo controllerInfo) {
        long count = controllerInfo
                .methods()
                .stream()
                .map(methodInfo -> new ControllerInfo.MethodInfo(methodInfo.name(), controllerInfo.prefixPath() + methodInfo.path(), null))
                .distinct()
                .count();

        return count == controllerInfo.methods().size();
    }

    private Set<ControllerInfo.MethodInfo> getAvailableMethods(Class<?> controllerClass) {
        Set<ControllerInfo.MethodInfo> availableMethods = new HashSet<>();

        for (Method method : controllerClass.getMethods())
            getMethodInfo(method).ifPresent(availableMethods::add);

        if (availableMethods.isEmpty())
            throw ControllerException.METHOD_NOT_FOUND;

        return availableMethods;
    }

    private Optional<ControllerInfo.MethodInfo> getMethodInfo(Method method) {
        int numberOfRestMethod = 0;
        ControllerInfo.MethodInfo info = null;
        for (Annotation annotation : method.getAnnotations()) {

            if (annotation instanceof GetMethod restMethodAnnotation) {
                info = new ControllerInfo.MethodInfo(HttpMethod.GET, emptyToFSlash(restMethodAnnotation.value()), method);
                numberOfRestMethod++;
            } else if (annotation instanceof PostMethod restMethodAnnotation) {
                info = new ControllerInfo.MethodInfo(HttpMethod.POST, emptyToFSlash(restMethodAnnotation.value()), method);
                numberOfRestMethod++;
            }

            if (numberOfRestMethod > 1)
                throw ControllerException.NOUN_UNIQUE_URI_MAPPING;
        }

        return Optional.ofNullable(info);

    }

    private String emptyToFSlash(String value) {
        return value == null || value.isEmpty() ? "/" : value;
    }

    private String getPathPrefix(Class<?> controllerClass) {

        if (!controllerClass.isAnnotationPresent(RestController.class))
            throw ControllerException.NOT_A_REST_CONTROLLER;

        return controllerClass.getAnnotation(RestController.class).value();
    }


    private boolean hasNotAnyRestMethod(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .flatMap(m -> Arrays.stream(m.getAnnotations()))
                .map(Annotation::annotationType)
                .noneMatch(VALID_REST_METHODS::contains);
    }


}

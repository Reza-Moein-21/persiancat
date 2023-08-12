package com.gmail.rezamoeinpe.persiancat.internal;

import com.gmail.rezamoeinpe.persiancat.*;
import com.gmail.rezamoeinpe.persiancat.exceptions.InvalidControllerException;
import com.gmail.rezamoeinpe.persiancat.exceptions.MethodNotFound;
import com.gmail.rezamoeinpe.persiancat.exceptions.NotRestController;
import com.gmail.rezamoeinpe.persiancat.exceptions.NounUniqueURIMapping;
import com.gmail.rezamoeinpe.persiancat.method.GetMethod;
import com.gmail.rezamoeinpe.persiancat.method.PostMethod;
import com.gmail.rezamoeinpe.persiancat.method.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ControllerParserImpl implements ControllerParser {
    public static final Logger LOGGER = LoggerFactory.getLogger(ControllerParserImpl.class);

    private final Set<Class<? extends Annotation>> VALID_REST_METHODS = Set.of(
            GetMethod.class,
            PostMethod.class);

    @Override
    public ControllerInfo parsController(Object controller) throws InvalidControllerException {
        Objects.requireNonNull(controller, "pars action required a non-null Controller");

        Class<?> controllerClass = controller.getClass();
        String pathPrefix = getPathPrefix(controllerClass);

        if (hasNotAnyRestMethod(controllerClass))
            throw new InvalidControllerException(new MethodNotFound());

        var methods = getAvailableMethods(controllerClass);


        var controllerInfo = new ControllerInfo(pathPrefix, methods);

        if (!areAllPathsUnique(controllerInfo))
            throw new InvalidControllerException(new NounUniqueURIMapping());


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
            throw new InvalidControllerException(new MethodNotFound());

        return availableMethods;
    }

    private Optional<ControllerInfo.MethodInfo> getMethodInfo(Method method) {
        int numberOfRestMethod = 0;
        ControllerInfo.MethodInfo info = null;
        for (Annotation annotation : method.getAnnotations()) {

            if (annotation instanceof GetMethod restMethodAnnotation) {
                info = new ControllerInfo.MethodInfo(MethodName.GET, emptyToFSlash(restMethodAnnotation.value()), method);
                numberOfRestMethod++;
            } else if (annotation instanceof PostMethod restMethodAnnotation) {
                info = new ControllerInfo.MethodInfo(MethodName.POST, emptyToFSlash(restMethodAnnotation.value()), method);
                numberOfRestMethod++;
            }

            if (numberOfRestMethod > 1)
                throw new InvalidControllerException(new NounUniqueURIMapping());
        }

        return Optional.ofNullable(info);

    }

    private String emptyToFSlash(String value) {
        return value == null || value.isEmpty() ? "/" : value;
    }

    private String getPathPrefix(Class<?> controllerClass) {

        if (!controllerClass.isAnnotationPresent(RestController.class))
            throw new InvalidControllerException(new NotRestController());

        return controllerClass.getAnnotation(RestController.class).value();
    }


    private boolean hasNotAnyRestMethod(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .flatMap(m -> Arrays.stream(m.getAnnotations()))
                .map(Annotation::annotationType)
                .noneMatch(VALID_REST_METHODS::contains);
    }
}

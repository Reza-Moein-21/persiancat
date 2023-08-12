package com.gmail.rezamoeinpe.persiancat.internal;

import com.gmail.rezamoeinpe.persiancat.MethodName;

import java.lang.reflect.Method;
import java.util.Set;

public record ControllerInfo(String prefixPath, Set<MethodInfo> methods) {
    public record MethodInfo(MethodName name, String path, Method actualMethod) {
    }
}

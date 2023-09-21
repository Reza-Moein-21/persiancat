package com.gmail.rezamoeinpe.persiancat.internal.controller;

import com.gmail.rezamoeinpe.persiancat.internal.http.HttpMethod;

import java.lang.reflect.Method;
import java.util.Set;

public record ControllerInfo(String prefixPath, Set<MethodInfo> methods) {
    public record MethodInfo(HttpMethod name, String path, Method actualMethod) {
    }
}

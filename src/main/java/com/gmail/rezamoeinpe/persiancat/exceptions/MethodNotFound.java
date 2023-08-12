package com.gmail.rezamoeinpe.persiancat.exceptions;

public class MethodNotFound extends Throwable {
    public MethodNotFound() {
        super("Controller has not any public method annotated as rest method");
    }
}

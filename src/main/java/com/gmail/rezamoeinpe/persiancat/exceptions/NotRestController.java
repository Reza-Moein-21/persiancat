package com.gmail.rezamoeinpe.persiancat.exceptions;

public class NotRestController extends Throwable {
    public NotRestController() {
        super("Controller should annotated with @RestController");
    }
}

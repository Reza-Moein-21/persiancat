package com.gmail.rezamoeinpe.persiancat.exceptions;

import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyException;
import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyExceptionInfo;
import com.gmail.rezamoeinpe.persiancat.internal.http.HttpStatus;

public final class ControllerException extends CattyException {
    private ControllerException(CattyExceptionInfo reason) {
        super(reason);
    }

    public static final ControllerException METHOD_NOT_FOUND = new ControllerException(new CattyExceptionInfo(String.valueOf(HttpStatus.NOT_IMPLEMENTED.getCode()), "Controller has not any public method annotated as rest method"));

    public static final ControllerException NOT_A_REST_CONTROLLER = new ControllerException(new CattyExceptionInfo(String.valueOf(HttpStatus.NOT_IMPLEMENTED.getCode()), "Controller should annotated with @RestController"));

    public static final ControllerException NOUN_UNIQUE_URI_MAPPING = new ControllerException(new CattyExceptionInfo(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.getCode()), "URL mapping should be unique"));


}

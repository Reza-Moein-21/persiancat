package com.gmail.rezamoeinpe.persiancat.exceptions.base;

import com.gmail.rezamoeinpe.persiancat.exceptions.ControllerException;

public abstract sealed class CattyException extends RuntimeException permits ControllerException {
    private final CattyCause cause;

    protected CattyException(CattyCause cause) {
        super(cause);
        this.cause = cause;
    }

    public CattyCause cause() {
        return cause;
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", getClass().getSimpleName(), cause.reason());
    }
}

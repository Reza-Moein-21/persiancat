package com.gmail.rezamoeinpe.persiancat.exceptions.base;

public abstract class CattyCause extends Throwable {
    private final String reason;

    protected CattyCause(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String reason() {
        return reason;
    }

}

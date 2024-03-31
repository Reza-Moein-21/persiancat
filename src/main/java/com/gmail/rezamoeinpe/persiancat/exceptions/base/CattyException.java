package com.gmail.rezamoeinpe.persiancat.exceptions.base;

public abstract class CattyException extends RuntimeException implements CattyError {
    private final CattyExceptionInfo info;

    protected CattyException(CattyExceptionInfo info) {
        this.info = info;
    }

    @Override
    public String getCode() {
        return info.code();
    }

    @Override
    public String getReason() {
        return info.reason();
    }

    @Override
    public String getAddition() {
        return info.addition();
    }

    @Override
    public String getMessage() {
        if (this.getAddition() == null)
            return String.format("[%s:%s]", this.getCode(), this.getReason());

        return String.format("[%s:%s, %s]", this.getCode(), this.getReason(), this.getAddition());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + this.getMessage();
    }
}

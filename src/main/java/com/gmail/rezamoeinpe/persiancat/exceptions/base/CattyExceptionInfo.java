package com.gmail.rezamoeinpe.persiancat.exceptions.base;

public record CattyExceptionInfo(String code, String reason, String addition) {

    public CattyExceptionInfo {
        if (code == null) throw new IllegalArgumentException("Code is required");
        if (reason == null) throw new IllegalArgumentException("Reason is required");
    }

    public CattyExceptionInfo(String code, String reason) {
        this(code, reason, null);
    }
}

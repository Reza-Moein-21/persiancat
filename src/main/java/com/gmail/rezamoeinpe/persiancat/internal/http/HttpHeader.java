package com.gmail.rezamoeinpe.persiancat.internal.http;

public record HttpHeader(String name, String value) {
    @Override
    public String toString() {
        return name + ": " + value;
    }
}

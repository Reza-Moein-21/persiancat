package com.gmail.rezamoeinpe.persiancat.internal.controller;

import com.gmail.rezamoeinpe.persiancat.exceptions.InvalidControllerException;

public interface ControllerParser {
    ControllerInfo parsController(Object controller) throws InvalidControllerException;
}

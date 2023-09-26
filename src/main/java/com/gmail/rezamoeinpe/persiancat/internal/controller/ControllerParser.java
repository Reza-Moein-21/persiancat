package com.gmail.rezamoeinpe.persiancat.internal.controller;

import com.gmail.rezamoeinpe.persiancat.exceptions.ControllerException;

public interface ControllerParser {
    ControllerInfo parsController(Object controller) throws ControllerException;
}

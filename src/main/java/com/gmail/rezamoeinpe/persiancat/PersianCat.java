package com.gmail.rezamoeinpe.persiancat;

import com.gmail.rezamoeinpe.persiancat.exceptions.ControllerException;

public interface PersianCat {
    /**
     * this would make the cat knead 😻
     * basically start the server
     */
    void knead();

    /**
     * Put your cat to sleep
     * AKA stop the server
     */
    void takeNap();

    /**
     * Giving array of objects that configured as a rest controller
     * to teach Cat how to handel requests
     *
     * @param controllers arrays of objects annotated as controller
     * @throws ControllerException In runtime all controller will check and throw this if any
     *                                    problem find
     */
    void setControllers(Object... controllers) throws ControllerException;
}

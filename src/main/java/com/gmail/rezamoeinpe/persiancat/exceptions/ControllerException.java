package com.gmail.rezamoeinpe.persiancat.exceptions;

import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyCause;
import com.gmail.rezamoeinpe.persiancat.exceptions.base.CattyException;

public final class ControllerException extends CattyException {
    public ControllerException(CattyCause cause) {
        super(cause);
    }

    public static class MethodNotFound extends CattyCause {
        public MethodNotFound() {
            super("Controller has not any public method annotated as rest method");
        }
    }

    public static class NotRestController extends CattyCause {
        public NotRestController() {
            super("Controller should annotated with @RestController");
        }
    }

    public static class NounUniqueURIMapping extends CattyCause {
        public NounUniqueURIMapping() {
            super("URL mapping should be unique");
        }
    }

}

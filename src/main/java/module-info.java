module com.gmail.rezamoeinpe.persiancat {
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires com.google.gson;

    // public exports
    exports com.gmail.rezamoeinpe.persiancat;
    exports com.gmail.rezamoeinpe.persiancat.rest.method;
    exports com.gmail.rezamoeinpe.persiancat.exceptions;
    exports com.gmail.rezamoeinpe.persiancat.exceptions.base;

    // only for test exports
    exports com.gmail.rezamoeinpe.persiancat.internal.controller to com.gmail.rezamoeinpe.persiancat.test;
    exports com.gmail.rezamoeinpe.persiancat.internal.http to com.gmail.rezamoeinpe.persiancat.test;
    exports com.gmail.rezamoeinpe.persiancat.internal.http.parser to com.gmail.rezamoeinpe.persiancat.test;
    exports com.gmail.rezamoeinpe.persiancat.internal.socket to com.gmail.rezamoeinpe.persiancat.test;
    exports com.gmail.rezamoeinpe.persiancat.internal.http.handler to com.gmail.rezamoeinpe.persiancat.test;
}
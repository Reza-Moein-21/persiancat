module com.gmail.rezamoeinpe.persiancat {
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires org.slf4j;

    exports com.gmail.rezamoeinpe.persiancat;
    exports com.gmail.rezamoeinpe.persiancat.rest.method;
    exports com.gmail.rezamoeinpe.persiancat.exceptions;
    exports com.gmail.rezamoeinpe.persiancat.exceptions.base;
}
package com.gmail.rezamoeinpe.persiancat.test.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestUtils {

    public static InputStream toInputStream(String st) {
        return new ByteArrayInputStream(st.getBytes(StandardCharsets.UTF_8));
    }
}

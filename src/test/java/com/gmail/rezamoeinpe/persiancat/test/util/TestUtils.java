package com.gmail.rezamoeinpe.persiancat.test.util;

import java.io.ByteArrayInputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

public class TestUtils {

    public static ReadableByteChannel toByteChannel(String st) {
        return Channels.newChannel(new ByteArrayInputStream(st.getBytes(StandardCharsets.UTF_8)));
    }
}

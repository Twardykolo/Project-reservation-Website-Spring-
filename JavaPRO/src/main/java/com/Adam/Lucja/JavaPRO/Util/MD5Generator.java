package com.Adam.Lucja.JavaPRO.Util;

import lombok.SneakyThrows;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Generator {
    @SneakyThrows
    public static String getMD5(String data)
    {
        MessageDigest md =MessageDigest.getInstance("MD5");
        md.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        String myHash = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return myHash;
    }
}

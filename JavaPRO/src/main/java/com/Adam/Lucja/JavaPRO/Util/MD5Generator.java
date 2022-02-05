package com.Adam.Lucja.JavaPRO.Util;

import lombok.SneakyThrows;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Generator {
    /**
     * Funkcja szyfrująca przekazany {@link String} używając szyfrowania md5.
     * @param data {@link String} do zaszyfrowania
     * @return {@link String}
     */
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

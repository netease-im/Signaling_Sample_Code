package com.netease.signalling.demo.utils;

import java.io.Closeable;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;

public class BaseUtil {

    private BaseUtil() {

    }

    public static String getStringMD5(String value) {
        if (value == null || value.trim().length() < 1) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return HexDump.toHex(md5.digest(value.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(HttpURLConnection connection) {
        if (connection == null) {
            return;
        }
        connection.disconnect();
    }
}

package com.datapyro.kafka.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashCodeUtil {

    public static String getHashString(Object... objects) throws NoSuchAlgorithmException, IOException {
        StringBuilder buffer = new StringBuilder();
        for (Object value : objects) {
            buffer.append(value == null ? "" : value);
            buffer.append("-");
        }
        return md5(buffer.toString().getBytes());
    }

    public static String md5(byte[] bytes) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);

        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}

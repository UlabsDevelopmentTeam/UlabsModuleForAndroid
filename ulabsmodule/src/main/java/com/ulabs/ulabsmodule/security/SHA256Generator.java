package com.ulabs.ulabsmodule.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by OH-Biz on 2017-11-22.
 */

public class SHA256Generator {
    public static String generate(String string){
        String SHA = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(string.getBytes());
            byte byteData[] = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }
}

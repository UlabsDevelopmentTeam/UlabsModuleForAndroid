package com.ulabs.ulabsmodule.security;

import android.security.keystore.KeyProperties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by OH-Biz on 2017-11-22.
 */

public class AESEncryptor {
    private static void encryptInternal(int optmode, byte[] inputText, ByteArrayOutputStream outputText){
        byte[] key = {9,8,7,6,5,4,3,2,1,0,9,8,7,6,5,4};
        byte[] iv = {1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};

        SecretKey AESKey = new SecretKeySpec(key, 0 ,key.length, "AES");

        try {
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(optmode, AESKey, new IvParameterSpec(iv));

            outputText.write(cipher.doFinal(inputText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void encrypt(byte[] inputText, ByteArrayOutputStream outputText){
        encryptInternal(Cipher.ENCRYPT_MODE, inputText, outputText);
    }

    public static void decrypt(byte[] inputText, ByteArrayOutputStream outputText){
        encryptInternal(Cipher.DECRYPT_MODE, inputText, outputText);
    }
}

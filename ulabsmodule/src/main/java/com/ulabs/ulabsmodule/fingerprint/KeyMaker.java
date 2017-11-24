package com.ulabs.ulabsmodule.fingerprint;

import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.ulabs.ulabsmodule.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by OH-Biz on 2017-11-20.
 */

public class KeyMaker {
    private static KeyMaker keyMaker;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;

    private Cipher cipher;
    private static final String KEY_NAME = "default_key";
    private KeyMakingFailureCallback callback;

    private KeyMaker() {
    }

    public static KeyMaker getInstance(){
        if(keyMaker == null){
            keyMaker = new KeyMaker();
        }
        return keyMaker;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void init(Context context) {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }

        KeyguardManager keyguardManager = context.getSystemService(KeyguardManager.class);
        if(!keyguardManager.isKeyguardSecure()){
            Toast.makeText(context, R.string.security_not_setting, Toast.LENGTH_LONG).show();
            if(callback != null){
                callback.onKeyMakingFail(context.getString(R.string.security_not_setting));
            }
            return;
        }

        FingerprintManager fingerprintManager = context.getSystemService(FingerprintManager.class);
        if(!fingerprintManager.hasEnrolledFingerprints()){
            Toast.makeText(context, R.string.fingerprint_is_not_exist, Toast.LENGTH_LONG).show();
            if(callback != null){
                callback.onKeyMakingFail(context.getString(R.string.fingerprint_is_not_exist));
            }
            return;
        }

        createKey(KEY_NAME, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        try {
            keyStore.load(null);

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (IOException |NoSuchAlgorithmException | CertificateException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean validate() {
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,  null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        }catch (KeyPermanentlyInvalidatedException e){
            return false;
        }
        catch (IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyStoreException | InvalidKeyException e) {
            return false;
        }
    }

    public Cipher getCipher() {
        return cipher;
    }

    public void setFailureCallback(KeyMakingFailureCallback callback) {
        this.callback = callback;
    }

    public interface KeyMakingFailureCallback{
        void onKeyMakingFail(String errorMsg);
    }
}

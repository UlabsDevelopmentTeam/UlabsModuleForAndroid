package com.ulabs.ulabsmodule.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.ulabs.ulabsmodule.R;

/**
 * Created by OH-Biz on 2017-11-20.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintUtil extends FingerprintManager.AuthenticationCallback{
    private FingerprintManager fingerprintManager;
    private FingerprintCallback callback;
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public FingerprintUtil(Context context) {
        fingerprintManager = context.getSystemService(FingerprintManager.class);
        mContext = context;
    }

    public void setOnFingerprintCallback(FingerprintCallback callback){
        this.callback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isFingerprintAvailable() {
        return fingerprintManager.isHardwareDetected();
    }

    public boolean hasFingerprint(){
        return fingerprintManager.hasEnrolledFingerprints();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startReading(FingerprintManager.CryptoObject cryptoObject) {
        if(!isFingerprintAvailable()){
            return;
        }
        fingerprintManager.authenticate(cryptoObject, null, 0 , this, null);
    }

    @Deprecated
    public void stopReading() {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Log.d("FingerprintUtil", "Authentication error: " + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Log.d("FingerprintUtil", "Authentication helper: " + helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        callback.onAuthenticated();
    }

    @Override
    public void onAuthenticationFailed() {
        callback.onError(mContext.getString(R.string.recognizing_fail_retry));
    }

}

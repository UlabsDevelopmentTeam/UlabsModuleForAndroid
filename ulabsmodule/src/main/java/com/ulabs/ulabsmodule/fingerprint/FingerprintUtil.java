package com.ulabs.ulabsmodule.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by OH-Biz on 2017-11-20.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintUtil extends FingerprintManager.AuthenticationCallback{
    private FingerprintManager fingerprintManager;
    private CancellationSignal cancellationSignal;
    private boolean selfCancelled;
    private FingerprintCallback callback;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public FingerprintUtil(Context context, FingerprintCallback callback) {
        fingerprintManager = context.getSystemService(FingerprintManager.class);
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
        cancellationSignal = new CancellationSignal();
        selfCancelled = false;
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0 , this, null);
    }

    public void stopReading() {
        if(cancellationSignal != null){
            selfCancelled = true;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        if(!selfCancelled){
            callback.onError(errString.toString());
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Log.d("ljm2006", "Authentication helper: " + helpString);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        callback.onAuthenticated();
    }

    @Override
    public void onAuthenticationFailed() {
        callback.onError("Authentication Failed...");
    }

}

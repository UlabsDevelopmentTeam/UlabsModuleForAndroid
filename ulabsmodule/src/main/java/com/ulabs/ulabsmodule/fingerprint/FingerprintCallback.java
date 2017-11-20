package com.ulabs.ulabsmodule.fingerprint;

/**
 * Created by OH-Biz on 2017-11-20.
 */

public interface FingerprintCallback {
    /**
     * 반드시 implement해야 한다.
     * */
    void onAuthenticated();
    void onError(String errString);
}

package com.ulabs.ulabsmodule.fingerprint;

/**
 * Created by OH-Biz on 2017-11-20.
 */

public interface FingerprintCallback {
    /**
     * must be implements
     * */
    void onAuthenticated();
    void onError(String errString);
}

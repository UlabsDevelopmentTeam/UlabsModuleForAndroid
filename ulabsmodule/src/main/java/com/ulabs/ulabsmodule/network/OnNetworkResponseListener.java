package com.ulabs.ulabsmodule.network;

/**
 * Created by OH-Biz on 2017-11-20.
 */

public interface OnNetworkResponseListener {
    void onResponse(String url, String result);
    void onErrorResponse(String url,String result);
    void onTimeOut(String url);
    void onRetry(String url);
    void onRetryAttemptFinished(String url);
}

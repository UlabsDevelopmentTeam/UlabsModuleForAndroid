package com.ulabs.ulabsmodule.network;

/**
 * Created by OH-Biz on 2017-11-20.
 */

public interface OnNetworkResponseListener {
    void onResponse(String requestURL, String result);
    void onErrorResponse(String result);
}

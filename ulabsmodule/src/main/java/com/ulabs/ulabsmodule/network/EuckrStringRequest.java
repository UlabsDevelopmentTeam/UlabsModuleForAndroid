package com.ulabs.ulabsmodule.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Created by OH-Biz on 2017-11-07.
 */

public class EuckrStringRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private String jsonString;

    public EuckrStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(method, url, errorListener);
        mListener = listener;
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            jsonString = new String (response.data, "euc-kr");
            return Response.success(new String(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            jsonString = new String(response.data);
        }
        return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}

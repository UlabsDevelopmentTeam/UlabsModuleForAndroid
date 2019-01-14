package com.ulabs.ulabsmodule.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by OH-Biz on 2017-11-07.
 */

public class UlabsNetwork{

    private RequestQueue requestQueue;
    private OnNetworkResponseListener callback;
    private static UlabsNetwork manager;

    public static final int METHOD_GET = 0;
    public static final int METHOD_POST = 1;
    public static final int METHOD_DELETE = 2;
    public static final int METHOD_PUT = 3;


    private UlabsNetwork(Context context) {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static UlabsNetwork getInstance(Context context){
        if(manager == null){
            manager = new UlabsNetwork(context);
        }
        return manager;
    }

    public void requireStringData(int method, final String url) {
        StringRequest request = new StringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }
            }
        });

        requestQueue.add(request);
    }


    public void requireStringData(int method, final String url, final Map<String, String> params) {

        StringRequest request = new StringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void requireEuckrStringData(int method, final String url, final Map<String,String> params){
        EuckrStringRequest request = new EuckrStringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(2500, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void require(Request request) {
        requestQueue.add(request);
    }


    public void setOnNetworkResponseListener(OnNetworkResponseListener listener) {
        callback = listener;
    }

    private int applyRequestMethod(int method){
        switch (method){
            case METHOD_GET:{
                return Request.Method.GET;

            }
            case METHOD_POST:{
                return Request.Method.POST;

            }
            case METHOD_DELETE:{
                return Request.Method.DELETE;

            }
            case METHOD_PUT:{
                return Request.Method.PUT;

            }
            default:{
                return Request.Method.GET;
            }
        }
    }
}

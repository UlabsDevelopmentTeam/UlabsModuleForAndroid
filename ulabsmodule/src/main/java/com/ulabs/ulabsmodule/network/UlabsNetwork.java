package com.ulabs.ulabsmodule.network;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
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

    private long retryInterval = 1500;

    private static final int SEND_MSG_RETRY = 0;
    private static final int SEND_MSG_RETRY_RESET = 1;

    private boolean performRetry = true;
    private static final int DEFAULT_RETRY_COUNT = 3;


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

    public void requireStringData(final int method, final String url) {
        StringRequest request = new StringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
                performRetry();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }

                if(performRetry){

                    if(error.fillInStackTrace().toString().toLowerCase().contains("volley")){
                        Message msg = new Message();
                        msg.obj = url;
                        msg.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putInt("method", method);
                        bundle.putBoolean("charset_euckr", false);
                        msg.setData(bundle);
                        internalHandler.sendMessageDelayed(msg,retryInterval);
                    }
                }else{
                    internalHandler.sendEmptyMessage(SEND_MSG_RETRY_RESET);
                }
            }
        });

        requestQueue.add(request);
    }


    public void requireStringData(final int method, final String url, final Map<String, String> params) {
        final StringRequest request = new StringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
                performRetry();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }
                if(performRetry){

                    if(error.fillInStackTrace().toString().toLowerCase().contains("volley")){
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("method", method);
                        bundle.putBoolean("charset_euckr", false);
                        bundle.putSerializable("params", (Serializable) params);
                        bundle.putInt("retryCount", DEFAULT_RETRY_COUNT);
                        msg.setData(bundle);
                        msg.obj = url;
                        msg.what = 0;
                        internalHandler.sendMessageDelayed(msg,retryInterval);
                    }
                }else{
                    internalHandler.sendEmptyMessage(SEND_MSG_RETRY_RESET);
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

    public void requireStringData(final int method, final String url, final Map<String, String> params, final int retryCount) {
        final StringRequest request = new StringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
                performRetry();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }
                if(performRetry){

                    if(error.fillInStackTrace().toString().toLowerCase().contains("volley")){
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("method", method);
                        bundle.putBoolean("charset_euckr", false);
                        bundle.putSerializable("params", (Serializable) params);
                        bundle.putInt("retryCount", retryCount);
                        msg.setData(bundle);
                        msg.obj = url;
                        msg.what = 0;
                        internalHandler.sendMessageDelayed(msg,retryInterval);
                    }
                }else{
                    internalHandler.sendEmptyMessage(SEND_MSG_RETRY_RESET);
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

    public void requireEuckrStringData(final int method, final String url, final Map<String,String> params){
        EuckrStringRequest request = new EuckrStringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
                performRetry();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }

                if(performRetry){

                    if(error.fillInStackTrace().toString().toLowerCase().contains("volley")){
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("method", method);
                        bundle.putBoolean("charset_euckr", true);
                        bundle.putSerializable("params", (Serializable) params);
                        bundle.putInt("retryCount", DEFAULT_RETRY_COUNT);
                        msg.setData(bundle);
                        msg.obj = url;
                        msg.what = 0;
                        internalHandler.sendMessageDelayed(msg,retryInterval);
                    }
                }else{
                    internalHandler.sendEmptyMessage(SEND_MSG_RETRY_RESET);
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

    public void requireEuckrStringData(final int method, final String url, final Map<String,String> params, final int retryCount){
        EuckrStringRequest request = new EuckrStringRequest(applyRequestMethod(method), url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(url, response);
                performRetry();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(url,error.fillInStackTrace().toString());
                if(error.fillInStackTrace().toString().toLowerCase().contains("timeout")){
                    callback.onTimeOut(url);
                }

                if(performRetry){

                    if(error.fillInStackTrace().toString().toLowerCase().contains("volley")){
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("method", method);
                        bundle.putBoolean("charset_euckr", true);
                        bundle.putSerializable("params", (Serializable) params);
                        bundle.putInt("retryCount", retryCount);
                        msg.setData(bundle);
                        msg.obj = url;
                        msg.what = 0;
                        internalHandler.sendMessageDelayed(msg,retryInterval);
                    }
                }else{
                    internalHandler.sendEmptyMessage(SEND_MSG_RETRY_RESET);
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

    public void setRetryInterval(long millis){
        this.retryInterval = millis;
    }

    public void performRetry(){
        performRetry = true;
    }

    public void cancelRetry(){
        performRetry = false;
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

    private Handler internalHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_MSG_RETRY:{

                    String url = (String) msg.obj;
                    Bundle bundle = msg.getData();
                    int method = bundle.getInt("method");
                    int retryCount = bundle.getInt("retryCount");
                    Map<String, String> params = (Map<String, String>) bundle.get("params");
                    boolean isEuckr = bundle.getBoolean("charset_euckr");

                    if(callback != null){
                        callback.onRetry(url);
                    }

                    if(retryCount-1 > 0){
                        if(isEuckr){
                            requireEuckrStringData(method, url, params, retryCount-1);
                        }else{
                            requireStringData(method, url, params, retryCount-1);
                        }
                    }else{
                        if(callback != null){
                            callback.onRetryAttemptFinished(url);
                        }
                    }
                    break;
                }
                case SEND_MSG_RETRY_RESET:{
                    performRetry();
                    break;
                }
            }
        }
    };
}

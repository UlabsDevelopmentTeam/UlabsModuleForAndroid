package com.ulabs.ulabsmodule.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by OH-Biz on 2017-09-20.
 */

public class ActivityDetector implements Application.ActivityLifecycleCallbacks {
    private static ActivityDetector detector;
    private boolean isActivityStarted = false;
    private Application application;
    private String stateDetectedClassName;
    private ActivityDetectionCallback detectionCallback;
    private int runningActivityCount = 0;

    public static final int ACTIVITY_RETURNED_TO_FOREGROUND = 1;
    public static final int ACTIVITY_FOREGROUND = 2;
    public static final int ACTIVITY_BACKGROUND = 0;

    private ActivityDetector(Application application) {
        this.application = application;
    }

    public static ActivityDetector getDetector(Application application) {
        if(detector == null){
            detector = new ActivityDetector(application);
        }
        return detector;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if(detectionCallback != null){
            detectionCallback.onActivityCreateDetected(activity.getLocalClassName());
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        runningActivityCount++;
        if(activity.getLocalClassName().equals(getStateDetectedClassName())){
            isActivityStarted = true;
        }

        if(detectionCallback != null){
            detectionCallback.onActivityStartDetected(activity.getLocalClassName());
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        runningActivityCount--;
        if(detectionCallback != null){
            detectionCallback.onActivityStopDetected(activity.getLocalClassName());
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if(activity.getLocalClassName().equals(getStateDetectedClassName())){
            isActivityStarted = false;
        }

        if(detectionCallback != null){
            detectionCallback.onActivityDestroyDetected(activity.getLocalClassName());
        }

    }

    public int getAppStatus(){
        switch (runningActivityCount){
            case 1:{
                return ACTIVITY_RETURNED_TO_FOREGROUND;
            }
            case 0:{
                return ACTIVITY_BACKGROUND;
            }
            default:{
                return ACTIVITY_FOREGROUND;
            }
        }
    }

    public void setDetectionCallback(ActivityDetectionCallback detectionCallback) {
        this.detectionCallback = detectionCallback;
    }

    public void removeDetectionCallback(){
        this.detectionCallback = null;
    }

    public boolean isActivityStarted(){
        return isActivityStarted;
    }

    public void register(){
        application.registerActivityLifecycleCallbacks(this);
    }

    public void unregister(){
        application.unregisterActivityLifecycleCallbacks(this);
    }

    public String getStateDetectedClassName() {
        return stateDetectedClassName;
    }

    public void setStateDetectedClassName(String stateDetectedClassName) {
        this.stateDetectedClassName = stateDetectedClassName;
    }
}

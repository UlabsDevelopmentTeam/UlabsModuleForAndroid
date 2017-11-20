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

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(activity.getLocalClassName().equals(getStateDetectedClassName())){
            isActivityStarted = true;
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

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if(activity.getLocalClassName().equals(getStateDetectedClassName())){
            isActivityStarted = false;
        }
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

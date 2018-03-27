package com.ulabs.ulabsmodule.util;

/**
 * Created by OH-Biz on 2018-01-16.
 */

public interface ActivityDetectionCallback {
    void onActivityCreateDetected(String activityName);
    void onActivityStartDetected(String activityName);
    void onActivityStopDetected(String activityName);
    void onActivityDestroyDetected(String activityName);
}

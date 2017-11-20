package com.ulabs.ulabsmodule.gps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Created by OH-Biz on 2017-09-05.
 */

public class GPSManager implements LocationListener{
    /**
     * GPSManager class
     * GPS를 이용하여 현재 위치의 위도, 경도를 얻을 수 있는 util class
     * 사용하기 위해서는 반드시 아래의 권한을 Manifest에 추가한다.
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
     * @singleton
     * */
    private Context mContext;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean isGetLocation = false;

    private Location location;
    private double latitude;//위도
    private double longitude;//경도

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATEDS = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;
    private static GPSManager manager;
    private LocationManager locationManager;

    private GPSManager(Context mContext) {
        this.mContext = mContext;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public static GPSManager getInstance(Context c){
        if(manager == null){
            manager = new GPSManager(c);
        }

        return manager;
    }
    /**
     * checkNetworkState
     * GPS 정보를 가지고 올 수 있는 Network 상태인지를 확인한다.
     * */
    private void checkNetworkState(){
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * loadGPSData
     * GPS를 이용하여 현재 위치의 위도, 경도를 저장한다.
     * */
    public void loadGPSData(){
        checkNetworkState();

        if(!isGPSEnabled && !isNetworkEnabled){
            isGetLocation = false;
            showGPSSettingDialog();
        }


        if(isNetworkEnabled){
            isGetLocation = true;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATEDS,
                    this);

            if(locationManager != null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        }

        if(isGPSEnabled){
            isGetLocation = true;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATEDS,
                    this);

            if(locationManager != null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location != null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        }

    }
    /**
     * stopLoadingGPS
     * GPS를 통한 위치 가져오기 작업을 중단한다.
     * */
    public void stopLoadingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }
    /**
     * showGPSSettingDialog
     * GPS 설정을 사용자가 활성화 하지 않았을 때 설정 앱 실행 시킬 수 있는 Alart를 보여준다.
     * */
    public void showGPSSettingDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("GPS 사용 유뮤 설정");
        dialog.setMessage("GPS 설정을 하지 않았을 가능성이 있습니다. 설정창으로 가시겠습니까?");
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /**
     * 위도, 경도에 대한 getter
     * */
    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public boolean isGetLocation() {
        return isGetLocation;
    }

    /**
     * 아래의 overriding 된 메소드는 LocationListener의 이벤트 callback 메소드이다.
     * */
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


package com.ulabs.ulabsmodule.gps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.ulabs.ulabsmodule.R;

/**
 * Created by OH-Biz on 2017-09-05.
 */

public class GPSManager implements LocationListener{

    private Context mContext;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean isGetLocation = false;

    private Location location;
    private double latitude;
    private double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATEDS = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;
    private static GPSManager manager;
    private LocationManager locationManager;

    private LocationChangeCallback locationChangeCallback;

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

    private void checkNetworkState(){
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void loadGPSData(){
        boolean locationPermissionGranted;

        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = false;
        }else{
            locationPermissionGranted = true;
        }

        if(locationPermissionGranted){
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

                return;
            }

            if(isGPSEnabled){
                isGetLocation = true;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
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
        }else{
            Toast.makeText(mContext, R.string.loading_gps_failed_because_permission_not_granted, Toast.LENGTH_LONG).show();
        }


    }

    public void stopLoadingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

    public void showGPSSettingDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(R.string.gps_usage_setting);
        dialog.setMessage(R.string.gps_might_not_be_setting);
        dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        dialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }

    public boolean isGetLocation() {
        return isGetLocation;
    }

    public void setOnLocationChangeCallback(LocationChangeCallback changeCallback){
        locationChangeCallback = changeCallback;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if(locationChangeCallback != null){
            locationChangeCallback.onLocationChanged(location);
        }

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


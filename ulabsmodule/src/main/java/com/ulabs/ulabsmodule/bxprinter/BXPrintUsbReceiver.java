package com.ulabs.ulabsmodule.bxprinter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

public class BXPrintUsbReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_PERMISSION = "com.ulabsmodule.bxprinter.USB_PERMISSION";
    private OnBXUSBPermissionCallback callback;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(ACTION_USB_PERMISSION.equals(action)){
            Log.d("ljm2006_UsbReceiver", "BXPrintUsbReceiver onReceived");
            Toast.makeText(context, "BXPrintUsbReceiver onReceived", Toast.LENGTH_SHORT).show();
            synchronized (this){
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
                    if(device != null){
                        Log.d("ljm2006_UsbReceiver", "USB Permission granted!!");
                        if(callback != null)
                            callback.onPermissionGranted();
                    }
                }else{
                    Log.d("ljm2006_UsbReceiver", "USB Permission denied...");
                }
            }
        }
    }

    public void setOnUSBPermissionCallback(OnBXUSBPermissionCallback callback){
        this.callback = callback;
    }
}

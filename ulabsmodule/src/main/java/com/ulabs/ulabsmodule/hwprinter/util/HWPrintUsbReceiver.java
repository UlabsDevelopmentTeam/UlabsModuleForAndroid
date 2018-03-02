package com.ulabs.ulabsmodule.hwprinter.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by OH-Biz on 2018-02-12.
 */

public class HWPrintUsbReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_PERMISSION = "com.ulabsmodule.hwprinter.util.USB_PERMISSION";
    public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";
    private OnUSBPermissionCallback callback;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(ACTION_USB_PERMISSION.equals(action)){
            Log.d("ljm2006_UsbReceiver", "PrintReceiver onReceived");
            synchronized (this){
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
                    if(device != null){
                        /*HWPrintDriver driver = new HWPrintDriver(context);
                        driver.setDevice();*/
                        Toast.makeText(context, "USB permission granted!", Toast.LENGTH_SHORT).show();
                        callback.onPermissionGranted();
                    }
                }else{
                    Log.d("ljm2006_UsbReceiver", "USB Permission denied...");
                }
            }
        }
    }

    public void setOnUSBPermissionCallback(OnUSBPermissionCallback callback){
        this.callback = callback;
    }
}

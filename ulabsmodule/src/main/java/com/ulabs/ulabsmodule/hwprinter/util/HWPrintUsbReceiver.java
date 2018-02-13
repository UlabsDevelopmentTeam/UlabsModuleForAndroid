package com.ulabs.ulabsmodule.hwprinter.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * Created by OH-Biz on 2018-02-12.
 */

public class HWPrintUsbReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_PERMISSION = "com.ulabsmodule.hwprinter.util.USB_PERMISSION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(ACTION_USB_PERMISSION.equals(action)){
            Log.d("ljm2006_UsbReceiver", "PrintReceiver onReceived");
            synchronized (this){
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
                    if(device != null){
                        HWPrintDriver driver = new HWPrintDriver(context);
                        driver.setDevice();
                    }
                }else{
                    Log.d("ljm2006_UsbReceiver", "USB Permission denied...");
                }
            }
        }
    }
}

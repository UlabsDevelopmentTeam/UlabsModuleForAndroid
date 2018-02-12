package com.ulabs.ulabsmodule.hwprinter.util;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

/**
 * Created by OH-Biz on 2018-02-12.
 */

public class HWPrintUsbReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_ATTACHED = UsbManager.ACTION_USB_ACCESSORY_ATTACHED;
    private OnUsbPermissionGrantedListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(ACTION_USB_ATTACHED.equals(action)){
            synchronized (this){
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_ATTACHED), 0);
                HWPrintDriver driver = new HWPrintDriver(context);
                driver.requestPermission(device, permissionIntent);
            }
        }
    }
}

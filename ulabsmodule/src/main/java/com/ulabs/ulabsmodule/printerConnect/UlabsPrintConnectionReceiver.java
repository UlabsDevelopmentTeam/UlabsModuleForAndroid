package com.ulabs.ulabsmodule.printerConnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

public class UlabsPrintConnectionReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_PERMISSION = "com.ulabsmodule.printConnect.USB_PERMISSION";
    public static final String ACTION_Q300_CONN = "com.ulabsmodule.printConnect.BLUETOOTH_CONNECTION";
    private OnConnectionListener listener;
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
                        Log.d("ljm2006_UsbReceiver", "USB Permission granted!!");
                        if(listener != null){
                            boolean containsVendorId = device.toString().contains("mVendorId=6");
                            if(containsVendorId){
                                listener.onHWPrinterConnected();
                            }else{

                                listener.onBXPrinterConnected(device);
                            }
                        }

                    }
                }else{
                    Log.d("ljm2006_UsbReceiver", "USB Permission denied...");
                }
            }
        }else if(ACTION_Q300_CONN.equals(action)){
            if(listener != null){
                String type = intent.getStringExtra("type");
                if(type.equals("bluetooth")){

                    listener.onQ300BluetoothPrinterConnected();
                }else if(type.equals("usb")){

                    listener.onQ300UsbPrinterConnected();
                }
            }
        }
    }

    public void setOnConnectionListener(OnConnectionListener listener) {
        this.listener = listener;
    }
}

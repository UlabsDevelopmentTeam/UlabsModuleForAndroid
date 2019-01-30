package com.ulabs.ulabsmodule.bxprinter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bixolon.labelprinter.BixolonLabelPrinter;

import java.util.Set;

public class BXPrinterManager {
    public static BXPrinterManager manager;
    private Context context;
    private BixolonLabelPrinter bixolonLabelPrinter;


    private BXPrinterManager(Context context) {
        this.context = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(usbConnectBR, filter);
        bixolonLabelPrinter = new BixolonLabelPrinter(context, internalHandler, null);
    }

    public static BXPrinterManager getInstance(Context context){
        if(manager == null){
            manager = new BXPrinterManager(context);
        }
        return manager;
    }

    private Handler internalHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case BixolonLabelPrinter.MESSAGE_STATE_CHANGE:{
                    switch (msg.arg1){
                        case BixolonLabelPrinter.STATE_CONNECTED:{
                            Toast.makeText(context, "connect!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case BixolonLabelPrinter.STATE_CONNECTING:{
                            Toast.makeText(context, "connecting...", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case BixolonLabelPrinter.STATE_NONE:{
                            Toast.makeText(context, "not connected T.T", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    break;
                }
                case BixolonLabelPrinter.MESSAGE_USB_DEVICE_SET:{
                    if(msg.obj == null){
                        Log.i("ulabsmodule","Bixolon usb devices not found!");
                    }else{

                        Set<UsbDevice> devices = (Set<UsbDevice>) msg.obj;
                        for(UsbDevice device : devices){
                            Toast.makeText(context, "connect! -? device name : " + device.getDeviceName(), Toast.LENGTH_SHORT).show();
                            bixolonLabelPrinter.connect(device);

                        }
                    }
                    break;
                }
            }
        }
    };

    private BroadcastReceiver usbConnectBR = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
                Toast.makeText(context, "Printer is connected!", Toast.LENGTH_SHORT).show();
                findUSBPrinter();
            }else{
                Toast.makeText(context, "Printer is disconnected!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /*public void setBixolonPrinterStatusListener(BXPrinterStatusListener listener){
        this.listener = listener;
    }*/

    public void findUSBPrinter(){
        bixolonLabelPrinter.findUsbPrinters();
    }

    public void disconnectPrinter(){
        if(bixolonLabelPrinter != null){
            bixolonLabelPrinter.disconnect();
            bixolonLabelPrinter = null;
        }
        if(manager != null){
            context.unregisterReceiver(usbConnectBR);
            manager = null;
        }
    }

    public void printBitmap(Bitmap bitmap){
        if(bixolonLabelPrinter.isConnected()){

            bixolonLabelPrinter.drawBitmap(bitmap,100,100, bitmap.getWidth(), 50);
            bixolonLabelPrinter.print(1,1);
        }else{
            Toast.makeText(context, "Printer is not connected!", Toast.LENGTH_SHORT).show();
            findUSBPrinter();
        }
    }
}

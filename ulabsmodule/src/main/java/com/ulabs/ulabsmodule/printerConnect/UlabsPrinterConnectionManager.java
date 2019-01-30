package com.ulabs.ulabsmodule.printerConnect;

import android.app.PendingIntent;
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
import com.ulabs.ulabsmodule.hwprinter.util.HWBitmapPrinting;
import com.ulabs.ulabsmodule.hwprinter.util.HWPrintDriver;
import com.ulabs.ulabsmodule.hwprinter.util.HWPrintUsbReceiver;
import com.ulabs.ulabsmodule.hwprinter.util.OnUSBPermissionCallback;

import java.util.Set;

public class UlabsPrinterConnectionManager {
    private static UlabsPrinterConnectionManager manager;
    private Context context;
    private BixolonLabelPrinter bixolonLabelPrinter;
    private boolean isBXConnected = false;
    private boolean isHWConnected = false;
    private HWPrintUsbReceiver hwPrintUsbReceiver;

    private UlabsPrinterConnectionManager(Context context) {
        this.context = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(usbConnectBR, filter);

        bixolonLabelPrinter = new BixolonLabelPrinter(context, internalHandler, null);
    }

    public static UlabsPrinterConnectionManager getInstance(Context context){
        if(manager == null){
            manager = new UlabsPrinterConnectionManager(context);
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
                            Toast.makeText(context, "Bixolon printer is connected!!!", Toast.LENGTH_SHORT).show();
                            isBXConnected = true;
                            break;
                        }
                        case BixolonLabelPrinter.STATE_CONNECTING:{
                            Toast.makeText(context, "connecting...", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case BixolonLabelPrinter.STATE_NONE:{
                            if(!isHWConnected){

                                hwPrintUsbReceiver = new HWPrintUsbReceiver();
                                hwPrintUsbReceiver.setOnUSBPermissionCallback(new OnUSBPermissionCallback() {
                                    @Override
                                    public void onPermissionGranted() {
                                        Toast.makeText(context, "Hwasung printer is connected!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(HWPrintUsbReceiver.ACTION_USB_PERMISSION), 0);
                                HWPrintDriver hwPrintDriver = new HWPrintDriver(context);
                                hwPrintDriver.requestPermission(pendingIntent);
                                isHWConnected = true;

                            }
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
                Toast.makeText(context, "Printer is found!", Toast.LENGTH_SHORT).show();
                findUSBPrinter();
            }else{
                Toast.makeText(context, "Printer is disconnected!", Toast.LENGTH_SHORT).show();
                isBXConnected = false;
                isHWConnected = false;
            }
        }
    };

    public void findUSBPrinter(){
        bixolonLabelPrinter.findUsbPrinters();
    }

    public void printBitmap(Bitmap bitmap){
        if(isBXConnected){

            bixolonLabelPrinter.drawBitmap(bitmap,100,100, bitmap.getWidth(), 50);
            bixolonLabelPrinter.print(1,1);
        }else if(isHWConnected){
            HWBitmapPrinting.generate(context, bitmap, HWBitmapPrinting.ALIGN_CENTER, 550, 480);
        }else{
            Toast.makeText(context, "Printer is not connected!", Toast.LENGTH_SHORT).show();
            findUSBPrinter();
        }
    }

    public boolean isBXConnected() {
        return isBXConnected;
    }

    public boolean isHWConnected() {
        return isHWConnected;
    }
}

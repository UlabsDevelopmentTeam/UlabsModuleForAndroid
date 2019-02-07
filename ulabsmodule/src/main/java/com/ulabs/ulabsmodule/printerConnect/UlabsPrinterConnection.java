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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class UlabsPrinterConnection {
    private static UlabsPrinterConnection manager;
    private final UlabsPrintConnectionReceiver connectionReceiver;
    private Context c;
    private BixolonLabelPrinter bixolonLabelPrinter;
    private boolean isBXConnected = false;
    private boolean isHWConnected = false;
    private OnConnectionListener connectionListener;
    private UsbManager usbManager;

    private UlabsPrinterConnection(Context context) {
        this.c = context;
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(usbConnectBR, filter);

        connectionReceiver = new UlabsPrintConnectionReceiver();
        connectionReceiver.setOnConnectionListener(new OnConnectionListener() {
            @Override
            public void onHWPrinterConnected() {
                Toast.makeText(c, "Hwasung printer is connected!!!", Toast.LENGTH_SHORT).show();
                isHWConnected = true;
            }

            @Override
            public void onBXPrinterConnected(UsbDevice device) {
                bixolonLabelPrinter.connect(device);
            }
        });
        c.registerReceiver(connectionReceiver, new IntentFilter(UlabsPrintConnectionReceiver.ACTION_USB_PERMISSION));

        bixolonLabelPrinter = new BixolonLabelPrinter(context, internalHandler, null);
    }

    public static UlabsPrinterConnection getInstance(Context context){
        if(manager == null){
            manager = new UlabsPrinterConnection(context);
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
                            Toast.makeText(c, "Bixolon printer is connected!!!", Toast.LENGTH_SHORT).show();
                            isBXConnected = true;
                            break;
                        }
                        case BixolonLabelPrinter.STATE_CONNECTING:{
                            Toast.makeText(c, "connecting...", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case BixolonLabelPrinter.STATE_NONE:{

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
                        if(devices.size() > 0){

                            for(UsbDevice device : devices){
                                bixolonLabelPrinter.connect(device);
                            }
                        }else{

                            Toast.makeText(c,"Bixolon usb devices not found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
            }
        }
    };


    private BroadcastReceiver usbConnectBR = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){

                Toast.makeText(context, "Printer is found!", Toast.LENGTH_SHORT).show();
                requestUSBPermission();
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

    public void requestUSBPermission(){
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Log.d("ljm2006", "DeviceName : " + device.getDeviceName());
            boolean containsVendorId = device.toString().contains("mVendorId=6");
            Toast.makeText(c, "containsVendorId : " + containsVendorId, Toast.LENGTH_SHORT).show();
            if(containsVendorId){
                Toast.makeText(c, "HW Condition...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UlabsPrintConnectionReceiver.ACTION_USB_PERMISSION);
                intent.putExtra("printerInfo",0);
                PendingIntent pendingBr = PendingIntent.getBroadcast(c, 0, intent ,0);
                usbManager.requestPermission(device, pendingBr);
            }else{
                usbManager.openDevice(device);
                Intent intent = new Intent(UlabsPrintConnectionReceiver.ACTION_USB_PERMISSION);
                intent.putExtra("printerInfo",1);
                PendingIntent pendingBr = PendingIntent.getBroadcast(c, 0, intent ,0);
                usbManager.requestPermission(device, pendingBr);

            }

        }
    }

    public void printBitmap(Bitmap bitmap){
        if(isBXConnected){

            bixolonLabelPrinter.drawBitmap(bitmap,100,100, bitmap.getWidth(), 50);
            bixolonLabelPrinter.print(1,1);
        }else if(isHWConnected){
            HWBitmapPrinting.generate(c, bitmap, HWBitmapPrinting.ALIGN_CENTER, 550, 480);
        }else{
            Toast.makeText(c, "Printer is not connected!", Toast.LENGTH_SHORT).show();
            findUSBPrinter();
        }
    }

    public void printBitmap(Bitmap bitmap, int horizontalStartPos, int verticalStartPos){
        if(isBXConnected){

            bixolonLabelPrinter.drawBitmap(bitmap,horizontalStartPos,verticalStartPos, bitmap.getWidth(), 50);
            bixolonLabelPrinter.print(1,1);
        }else if(isHWConnected){
            HWBitmapPrinting.generate(c, bitmap, HWBitmapPrinting.ALIGN_CENTER, 550, 480);
        }else{
            Toast.makeText(c, "Printer is not connected!", Toast.LENGTH_SHORT).show();
            findUSBPrinter();
        }
    }

    public boolean isBXConnected() {
        return isBXConnected;
    }

    public boolean isHWConnected() {
        return isHWConnected;
    }

    public void disconnect(){
        bixolonLabelPrinter.disconnect();
        c.unregisterReceiver(usbConnectBR);
        c.unregisterReceiver(connectionReceiver);
        manager = null;
    }
}

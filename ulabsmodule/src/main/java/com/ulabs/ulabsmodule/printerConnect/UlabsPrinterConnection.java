package com.ulabs.ulabsmodule.printerConnect;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.bxl.config.editor.BXLConfigLoader;
import com.ulabs.ulabsmodule.hwprinter.util.HWBitmapPrinting;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.config.JposEntry;

public class UlabsPrinterConnection {
    private static UlabsPrinterConnection manager;
    private final UlabsPrintConnectionReceiver connectionReceiver;
    private Context c;
    private BixolonLabelPrinter bixolonLabelPrinter;
    private boolean isBXConnected = false;
    private boolean isHWConnected = false;
    private boolean isQ300BluetoothConnected = false;
    private boolean isQ300UsbConnected = false;

    private UsbManager usbManager;
    private int bixolonStateNoneCnt = 0;

    private POSPrinter posPrinter;
    private BXLConfigLoader bxlConfigLoader;
    private final String bluetoothDeviceName = "SRP-Q300";
    private final String q300DeviceName = BXLConfigLoader.PRODUCT_NAME_SRP_Q300;
    private final int bluetoothPortType = BXLConfigLoader.DEVICE_BUS_BLUETOOTH;
    private final int usbPortType = BXLConfigLoader.DEVICE_BUS_USB;
    private String bluetoothDeviceAddress = "";

    public static final String ESCAPE_CHARACTERS = new String(new byte[] {0x1b, 0x7c});

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

            @Override
            public void onQ300BluetoothPrinterConnected() {
                Toast.makeText(c, "Bixolon bluetooth printer is connected!!!", Toast.LENGTH_SHORT).show();
                isQ300BluetoothConnected = true;
                try {
                    posPrinter.cutPaper(100);
                } catch (JposException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onQ300UsbPrinterConnected() {
                Toast.makeText(c, "Bixolon usb printer is connected!!!", Toast.LENGTH_SHORT).show();
                isQ300UsbConnected = true;
                String cutPaper = ESCAPE_CHARACTERS + "100fP";// feed and cut
                try {
                    posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, cutPaper);
                } catch (JposException e) {
                    e.printStackTrace();
                }
            }
        });
        IntentFilter filter_connectionReceiver = new IntentFilter();
        filter_connectionReceiver.addAction(UlabsPrintConnectionReceiver.ACTION_USB_PERMISSION);
        filter_connectionReceiver.addAction(UlabsPrintConnectionReceiver.ACTION_Q300_CONN);
        c.registerReceiver(connectionReceiver, filter_connectionReceiver);

        bixolonLabelPrinter = new BixolonLabelPrinter(context, internalHandler, null);
        posPrinter = new POSPrinter(context);
        bxlConfigLoader = new BXLConfigLoader(context);
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
//                            Q300 관련 USB 연결처리 로직
                            if(bixolonStateNoneCnt < 1){
                                usbQ300PrinterOpen();
                            }
                            bixolonStateNoneCnt++;
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
                isQ300BluetoothConnected = false;
                isQ300UsbConnected = false;
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

    public void connectPairedBluetoothDevice(Activity activity){
        if(Build.VERSION.SDK_INT >= 21){

            if(ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> pairedDeviceSet = bluetoothAdapter.getBondedDevices();
                for(BluetoothDevice device : pairedDeviceSet){
                    if(device.getName().contains("Q300")){
                        bluetoothDeviceAddress = device.getAddress();
                        bluetoothQ300PrinterOpen();
                        break;
                    }
                }
            }else{
                requestLocationPermission(activity);
            }

        }
    }

    private boolean initBXQ300BluetoothConfig(){
        try {
            bxlConfigLoader.openFile();
        } catch (Exception e) {
            bxlConfigLoader.newFile();
        }

        try {
            for(Object entry : bxlConfigLoader.getEntries()){
                JposEntry jposEntry = (JposEntry) entry;
                if(jposEntry.getLogicalName().equals(bluetoothDeviceName)){
                    bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
                }
            }

            bxlConfigLoader.addEntry(bluetoothDeviceName, BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER, q300DeviceName, bluetoothPortType, bluetoothDeviceAddress);

            bxlConfigLoader.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean initBXQ300USBConfig(){
        try {
            bxlConfigLoader.openFile();
        } catch (Exception e) {
            bxlConfigLoader.newFile();
        }

        try {
            for(Object entry : bxlConfigLoader.getEntries()){
                JposEntry jposEntry = (JposEntry) entry;
                if(jposEntry.getLogicalName().equals(bluetoothDeviceName)){
                    bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
                }
            }

            bxlConfigLoader.addEntry(bluetoothDeviceName, BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER, q300DeviceName, usbPortType, bluetoothDeviceAddress);

            bxlConfigLoader.saveFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void bluetoothQ300PrinterOpen(){
        if(initBXQ300BluetoothConfig()){

            try {
                posPrinter.open(q300DeviceName);
                posPrinter.claim(5000);
                posPrinter.setDeviceEnabled(true);
                posPrinter.setAsyncMode(true);

                Intent intent = new Intent(UlabsPrintConnectionReceiver.ACTION_Q300_CONN);
                intent.putExtra("type","bluetooth");
                c.sendBroadcast(intent);
            } catch (JposException e) {
                try {
                    posPrinter.close();
                } catch (JposException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    private void usbQ300PrinterOpen(){
        if(initBXQ300USBConfig()){
            try {
                posPrinter.open(q300DeviceName);
                posPrinter.claim(5000);
                posPrinter.setDeviceEnabled(true);
                posPrinter.setAsyncMode(true);

                Intent intent = new Intent(UlabsPrintConnectionReceiver.ACTION_Q300_CONN);
                intent.putExtra("type","usb");
                c.sendBroadcast(intent);
            } catch (JposException e) {
                try {
                    posPrinter.close();
                } catch (JposException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }

        }
    }

    public void printBitmap(Bitmap bitmap){
        if(isBXConnected){

            bixolonLabelPrinter.drawBitmap(bitmap,100,100, bitmap.getWidth(), 50);
            bixolonLabelPrinter.print(1,1);
        }else if(isHWConnected){

            HWBitmapPrinting.generate(c, bitmap, HWBitmapPrinting.ALIGN_CENTER, 550, 480);
        }else if(isQ300BluetoothConnected || isQ300UsbConnected) {

            try {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,550,480,matrix, false);
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
                buffer.put((byte) 75);
                buffer.put((byte) 0x01);
                buffer.put((byte) 0x00);
                posPrinter.printBitmap(buffer.getInt(0), rotatedBitmap, 445, POSPrinterConst.PTR_BM_CENTER);
                String cutPaper = ESCAPE_CHARACTERS + "100fP";// feed and cut
                posPrinter.printNormal(POSPrinterConst.PTR_S_RECEIPT, cutPaper);
//                posPrinter.cutPaper(100);
                rotatedBitmap.recycle();
            } catch (JposException e) {
                e.printStackTrace();
            }
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

    public void requestLocationPermission(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 6969);
    }

    public boolean isBXConnected() {
        return isBXConnected;
    }

    public boolean isHWConnected() {
        return isHWConnected;
    }

    public boolean isQ300BluetoothConnected() {
        return isQ300BluetoothConnected;
    }

    public boolean isQ300UsbConnected() {
        return isQ300UsbConnected;
    }

    public void disconnect(){
        bixolonLabelPrinter.disconnect();
        c.unregisterReceiver(usbConnectBR);
        c.unregisterReceiver(connectionReceiver);
        bixolonStateNoneCnt = 0;
        try {
            posPrinter.close();
        } catch (JposException e) {
            e.printStackTrace();
        }
        manager = null;
    }
}

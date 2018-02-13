package com.ulabs.ulabsmodule.hwprinter.util;

import android.app.PendingIntent;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by OH-Biz on 2018-01-26.
 */

public class HWPrintDriver implements HWPrinterDriverInterface{
    private int printerCount = 0;
    private UsbDevice usbDevice;
    private UsbManager usbManager;
    private UsbDeviceConnection usbDeviceConnection;
    private UsbEndpoint usbEndpoint;
    private Context context;

    public HWPrintDriver(Context context) {
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.context = context;
    }

    @Override
    public char setDevice() {
        char device_cnt = 0;

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Log.d("ljm2006","DeviceList : " + device.toString());
            boolean containsVendorId = device.toString().contains("mVendorId=6");
            if(containsVendorId){
                usbDevice = device;
                device_cnt++;
            }else{
                device_cnt = 0;
            }
        }

        if(device_cnt == 0){
            Log.d("ljm2006_HWPrintDriver", "Can not find Hwasung USB Device!");
            return 0;
        }

        UsbInterface usbInterface = usbDevice.getInterface(0);
        UsbEndpoint usbEndpoint = usbInterface.getEndpoint(0);

        if(usbEndpoint.getType() != UsbConstants.USB_ENDPOINT_XFER_BULK){
            Log.d("ljm2006_HWPrintDriver", "endpoint is not BULK type!");
            return 0;
        }

        this.usbEndpoint = usbEndpoint;

        if(usbDevice != null){
            UsbDeviceConnection connection = usbManager.openDevice(usbDevice);
            if(connection != null && connection.claimInterface(usbInterface, true)){
                this.usbDeviceConnection = connection;
            }
        }

        printerCount = device_cnt;

        return device_cnt;
    }

    @Override
    public void sendCommand(int[] intBuf, int dataCnt) {
        if(usbDeviceConnection != null){
            byte[] mByteBuf = new byte[dataCnt];
            UsbInterface usbInterface = usbDevice.getInterface(0);
            UsbEndpoint endpoint = usbInterface.getEndpoint(0);

            for(int i = 0 ; i < dataCnt ; i++){
                mByteBuf[i] = (byte) intBuf[i];
            }

            usbDeviceConnection.bulkTransfer(endpoint, mByteBuf, dataCnt, 0);
        }
    }

    @Override
    public int getPrinterStatus() {
        if(printerCount != 0){
            int dataCnt = 6;
            int[] mIntBuf = new int[dataCnt];

            mIntBuf[0] = 0x10;
            mIntBuf[1] = 0xaa;
            mIntBuf[2] = 0x55;
            mIntBuf[3] = 0x80;
            mIntBuf[4] = 0x54;
            mIntBuf[5] = 0xab;

            sendCommand(mIntBuf, dataCnt);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(usbDeviceConnection != null){
                byte[] mByteBuf = new byte[1];

                usbDeviceConnection.bulkTransfer(usbEndpoint, mByteBuf, 1, 1000);
                mIntBuf[0] = mByteBuf[0];
            }

            return mIntBuf[0];
        }
        return -1;
    }

    public void requestPermission(PendingIntent permissionIntent){
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        usbManager.requestPermission(usbDevice, permissionIntent);

        while (deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Log.d("ljm2006", "DeviceName : " + device.getDeviceName());
            usbManager.requestPermission(device, permissionIntent);
        }
    }
}

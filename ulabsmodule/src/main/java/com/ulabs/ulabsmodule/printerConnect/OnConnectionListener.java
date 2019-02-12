package com.ulabs.ulabsmodule.printerConnect;

import android.hardware.usb.UsbDevice;

public interface OnConnectionListener {
    void onHWPrinterConnected();
    void onBXPrinterConnected(UsbDevice usbDevice);
    void onQ300BluetoothPrinterConnected();
    void onQ300UsbPrinterConnected();
}

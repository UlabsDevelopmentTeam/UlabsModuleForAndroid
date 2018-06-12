package com.ulabs.ulabsmodule.util;

public interface BarcodeReaderInterface {
    void startRead(int action);
    void append(int keyCode, int action);
    void stopRead(int action);
}

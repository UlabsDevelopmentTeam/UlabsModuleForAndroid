package com.ulabs.ulabsmodule.hwprinter.util;

/**
 * Created by OH-Biz on 2018-01-26.
 */

public interface HWPrinterDriverInterface {

    // printer commands
    byte CMD_DLE = 0x10;
    byte CMD_ESC = 0x1b;
    byte CMD_GS = 0x1d;
    byte CMD_FS = 0x1c;
    byte CMD_SUB = 0x1a;

    char setDevice();
    void sendCommand(int[] intBuf, int dataCnt);
    int getPrinterStatus();
}

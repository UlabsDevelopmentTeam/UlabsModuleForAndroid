package com.ulabs.ulabsmodule.hwprinter.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by OH-Biz on 2018-01-26.
 */

public class HWBitmapPrinting {
    private static final int MAX_IMAGE_LINE = 192;

    public static final int DEFAULT_PRINT_WIDTH = 550;
    public static final int DEFAULT_PRINT_HEIGHT = 480;

    public static void generate(Context context, Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, DEFAULT_PRINT_WIDTH, DEFAULT_PRINT_HEIGHT ,matrix, false);

        HWPrintDriver printDriver = new HWPrintDriver(context);
        printDriver.setDevice();

        int bitmapHeight = rotatedBitmap.getHeight();
        int bitmapWidth = rotatedBitmap.getWidth();

        Log.d("ljm2006", "Width, height : " + bitmapWidth + " ," + bitmapHeight);
        byte rest = 0;
        int blockCnt;
        int printLine;
        int bitToByte;
        int bit_p;
        int k = 0;

        int pixels[];
        int intBuf[];
        int intBuf2[] = new int[128];

        int byteWidth = bitmapWidth / 8;

        int x ,y;


        int i = bitmapWidth % 8;
        int j;
        if(i > 0){
            byteWidth = byteWidth + 1;
            rest = 1;
        }

        pixels = new int[bitmapWidth * MAX_IMAGE_LINE];
        intBuf = new int[byteWidth * MAX_IMAGE_LINE];

        intBuf2[0] = HWPrintDriver.CMD_SUB;
        intBuf2[1] = 's';
        intBuf2[2] = 6;
        printDriver.sendCommand(intBuf2, 3);

        x = y = 0;

        blockCnt = bitmapHeight / MAX_IMAGE_LINE;

        if((bitmapHeight % MAX_IMAGE_LINE) > 0){
            blockCnt += 1;
        }

        for(k = 0 ; k < blockCnt ; k++){
            if(bitmapHeight >= MAX_IMAGE_LINE){
                printLine = MAX_IMAGE_LINE;
                bitmapHeight = bitmapHeight - printLine;
            }else{
                printLine = bitmapHeight;
            }

            rotatedBitmap.getPixels(pixels, 0 , bitmapWidth , x, y , bitmapWidth, printLine);

            j = 0;
            bitToByte = 0;
            bit_p = 0;


            for(i = 0 ; i < (bitmapWidth * printLine) ; i++){
                switch (bit_p % 8){
                    case 0 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x80;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit7!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit7!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                    case 1 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x40;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit6!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit6!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                    case 2 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x20;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit5!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit5!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                    case 3 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x10;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit4!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit4!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                    case 4 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x08;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit3!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit3!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                    case 5 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x04;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit2!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit2!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                    case 6 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x02;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit1!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit1!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                    case 7 :{
                        if(pixels[i] == 0xff000000){
                            bitToByte |= 0x01;
                        }
                        if(pixels[i] != 0xff000000 && pixels[i] != 0xffffffff){
                            Toast.makeText(context, "Error bit0!! => pixel value : " + Integer.valueOf(pixels[i]), Toast.LENGTH_SHORT).show();
//                            Log.d("ljm2006", "Error bit0!! => pixel value : " + Integer.valueOf(pixels[j]));
                            return;
                        }
                        break;
                    }
                }

                if ((bit_p % 8) == 7){
                    intBuf[j] = bitToByte;
                    bitToByte = 0;
                    j++;
                }

                bit_p++;

                if((i % bitmapWidth) == (bitmapWidth-1)){
                    if(rest == 1){
                        intBuf[j] = bitToByte;
                        bitToByte = 0;
                        bit_p = 0;
                        j++;
                    }
                }
            }
            // righting print position
            intBuf[0] = HWPrinterDriverInterface.CMD_ESC;
            intBuf[1] = 0x61;
            intBuf[2] = 0x02;
            // call send data
            printDriver.sendCommand(intBuf,3);

            /*// centering print position
            intBuf[0] = CMD_ESC;
            intBuf[1] = 0x61;
            intBuf[2] = 0x01;
            // call send data
            printDriver.sendCommand(intBuf,3);*/

            // send raster bitimage command 8byte
            intBuf2[0] = HWPrintDriver.CMD_GS;
            intBuf2[1] = 'v';
            intBuf2[2] = '0';
            intBuf2[3] = 0x00;
            intBuf2[4] = byteWidth % 256;
            intBuf2[5] = byteWidth / 256;
            intBuf2[6] = printLine % 256;
            intBuf2[7] = printLine / 256;

            printDriver.sendCommand(intBuf2, 8);

            int dataCnt = byteWidth * printLine;
            printDriver.sendCommand(intBuf, dataCnt);
            y += printLine;
        }

        // set the default print speed
        // print speed command 3byte

        intBuf2[0] = HWPrintDriver.CMD_SUB;
        intBuf2[1] = 's';
        intBuf2[2] = 14;
        printDriver.sendCommand(intBuf2, 3);
//        feeding
        intBuf[0] = 0x0a;
        intBuf[1] = 0x0a;
        intBuf[2] = 0x0a;
        intBuf[3] = 0x0a;
        intBuf[4] = 0x0a;
        printDriver.sendCommand(intBuf,5);

        // full cutting
        intBuf[0] = HWPrinterDriverInterface.CMD_ESC;
        intBuf[1] = 0x69;
        printDriver.sendCommand(intBuf,2);
    }
}

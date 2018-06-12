package com.ulabs.ulabsmodule.util;

import android.util.Log;

public class BarcodeReader implements BarcodeReaderInterface{
    private StringBuilder sb0;
    private StringBuilder sb1;
    //NUMBER
    private final int KEYCODE_0 = 7;
    private final int KEYCODE_1 = 8;
    private final int KEYCODE_2 = 9;
    private final int KEYCODE_3 = 10;
    private final int KEYCODE_4 = 11;
    private final int KEYCODE_5 = 12;
    private final int KEYCODE_6 = 13;
    private final int KEYCODE_7 = 14;
    private final int KEYCODE_8 = 15;
    private final int KEYCODE_9 = 16;
    //ALPHABET
    private final int KEYCODE_A = 29;
    private final int KEYCODE_B = 30;
    private final int KEYCODE_C = 31;
    private final int KEYCODE_D = 32;
    private final int KEYCODE_E = 33;
    private final int KEYCODE_F = 34;
    private final int KEYCODE_G = 35;
    private final int KEYCODE_H = 36;
    private final int KEYCODE_I = 37;
    private final int KEYCODE_J = 38;
    private final int KEYCODE_K = 39;
    private final int KEYCODE_L = 40;
    private final int KEYCODE_M = 41;
    private final int KEYCODE_N = 42;
    private final int KEYCODE_O = 43;
    private final int KEYCODE_P = 44;
    private final int KEYCODE_Q = 45;
    private final int KEYCODE_R = 46;
    private final int KEYCODE_S = 47;
    private final int KEYCODE_T = 48;
    private final int KEYCODE_U = 49;
    private final int KEYCODE_V = 50;
    private final int KEYCODE_W = 51;
    private final int KEYCODE_X = 52;
    private final int KEYCODE_Y = 53;
    private final int KEYCODE_Z = 54;
//    ENTER
    private final int KEYCODE_ENTER = 66;

    private BarcodeReaderCompleteCallback callback;

    private final int ACTION_DOWN = 0;
    private final int ACTION_UP = 1;
    private boolean isReadingUpAction = false;
    private boolean isReadingDownAction = false;
    private String result;
    private String result2;

    public BarcodeReader() {
        sb0 = new StringBuilder();
        sb1 = new StringBuilder();
    }

    @Override
    public void startRead(int action) {
        switch (action){
            case ACTION_DOWN:{
                isReadingDownAction = true;
                break;
            }
            case ACTION_UP:{
                isReadingUpAction = true;
                break;
            }
        }
    }

    @Override
    public void append(int keyCode, int action) {
        if(action == ACTION_DOWN){
            switch (keyCode){
                case KEYCODE_0:{
                    sb0.append(0);
                    break;
                }
                case KEYCODE_1:{
                    sb0.append(1);
                    break;
                }
                case KEYCODE_2:{
                    sb0.append(2);
                    break;
                }
                case KEYCODE_3:{
                    sb0.append(3);
                    break;
                }
                case KEYCODE_4:{
                    sb0.append(4);
                    break;
                }
                case KEYCODE_5:{
                    sb0.append(5);
                    break;
                }
                case KEYCODE_6:{
                    sb0.append(6);
                    break;
                }
                case KEYCODE_7:{
                    sb0.append(7);
                    break;
                }
                case KEYCODE_8:{
                    sb0.append(8);
                    break;
                }
                case KEYCODE_9:{
                    sb0.append(9);
                    break;
                }
                case KEYCODE_A:{
                    sb0.append("A");
                    break;
                }
                case KEYCODE_B:{
                    sb0.append("B");
                    break;
                }
                case KEYCODE_C:{
                    sb0.append("C");
                    break;
                }
                case KEYCODE_D:{
                    sb0.append("D");
                    break;
                }
                case KEYCODE_E:{
                    sb0.append("E");
                    break;
                }
                case KEYCODE_F:{
                    sb0.append("F");
                    break;
                }
                case KEYCODE_G:{
                    sb0.append("G");
                    break;
                }
                case KEYCODE_H:{
                    sb0.append("H");
                    break;
                }
                case KEYCODE_I:{
                    sb0.append("I");
                    break;
                }
                case KEYCODE_J:{
                    sb0.append("J");
                    break;
                }
                case KEYCODE_K:{
                    sb0.append("K");
                    break;
                }
                case KEYCODE_L:{
                    sb0.append("L");
                    break;
                }
                case KEYCODE_M:{
                    sb0.append("M");
                    break;
                }
                case KEYCODE_N:{
                    sb0.append("N");
                    break;
                }
                case KEYCODE_O:{
                    sb0.append("O");
                    break;
                }
                case KEYCODE_P:{
                    sb0.append("P");
                    break;
                }
                case KEYCODE_Q:{
                    sb0.append("Q");
                    break;
                }
                case KEYCODE_R:{
                    sb0.append("R");
                    break;
                }
                case KEYCODE_S:{
                    sb0.append("S");
                    break;
                }
                case KEYCODE_T:{
                    sb0.append("T");
                    break;
                }
                case KEYCODE_U:{
                    sb0.append("U");
                    break;
                }
                case KEYCODE_V:{
                    sb0.append("V");
                    break;
                }
                case KEYCODE_W:{
                    sb0.append("W");
                    break;
                }
                case KEYCODE_X:{
                    sb0.append("X");
                    break;
                }
                case KEYCODE_Y:{
                    sb0.append("Y");
                    break;
                }
                case KEYCODE_Z:{
                    sb0.append("Z");
                    break;
                }
                case KEYCODE_ENTER:{
                    stopRead(action);
                    break;
                }
                default:{
                    if(callback != null)
                        callback.readFailed("Unreadable barcode data...");

                    break;
                }
            }
        }else{
            switch (keyCode){
                case KEYCODE_0:{
                    sb1.append(0);
                    break;
                }
                case KEYCODE_1:{
                    sb1.append(1);
                    break;
                }
                case KEYCODE_2:{
                    sb1.append(2);
                    break;
                }
                case KEYCODE_3:{
                    sb1.append(3);
                    break;
                }
                case KEYCODE_4:{
                    sb1.append(4);
                    break;
                }
                case KEYCODE_5:{
                    sb1.append(5);
                    break;
                }
                case KEYCODE_6:{
                    sb1.append(6);
                    break;
                }
                case KEYCODE_7:{
                    sb1.append(7);
                    break;
                }
                case KEYCODE_8:{
                    sb1.append(8);
                    break;
                }
                case KEYCODE_9:{
                    sb1.append(9);
                    break;
                }
                case KEYCODE_A:{
                    sb1.append("A");
                    break;
                }
                case KEYCODE_B:{
                    sb1.append("B");
                    break;
                }
                case KEYCODE_C:{
                    sb1.append("C");
                    break;
                }
                case KEYCODE_D:{
                    sb1.append("D");
                    break;
                }
                case KEYCODE_E:{
                    sb1.append("E");
                    break;
                }
                case KEYCODE_F:{
                    sb1.append("F");
                    break;
                }
                case KEYCODE_G:{
                    sb1.append("G");
                    break;
                }
                case KEYCODE_H:{
                    sb1.append("H");
                    break;
                }
                case KEYCODE_I:{
                    sb1.append("I");
                    break;
                }
                case KEYCODE_J:{
                    sb1.append("J");
                    break;
                }
                case KEYCODE_K:{
                    sb1.append("K");
                    break;
                }
                case KEYCODE_L:{
                    sb1.append("L");
                    break;
                }
                case KEYCODE_M:{
                    sb1.append("M");
                    break;
                }
                case KEYCODE_N:{
                    sb1.append("N");
                    break;
                }
                case KEYCODE_O:{
                    sb1.append("O");
                    break;
                }
                case KEYCODE_P:{
                    sb1.append("P");
                    break;
                }
                case KEYCODE_Q:{
                    sb1.append("Q");
                    break;
                }
                case KEYCODE_R:{
                    sb1.append("R");
                    break;
                }
                case KEYCODE_S:{
                    sb1.append("S");
                    break;
                }
                case KEYCODE_T:{
                    sb1.append("T");
                    break;
                }
                case KEYCODE_U:{
                    sb1.append("U");
                    break;
                }
                case KEYCODE_V:{
                    sb1.append("V");
                    break;
                }
                case KEYCODE_W:{
                    sb1.append("W");
                    break;
                }
                case KEYCODE_X:{
                    sb1.append("X");
                    break;
                }
                case KEYCODE_Y:{
                    sb1.append("Y");
                    break;
                }
                case KEYCODE_Z:{
                    sb1.append("Z");
                    break;
                }
                case KEYCODE_ENTER:{
                    stopRead(action);
                    break;
                }
                default:{
                    if(callback != null)
                        callback.readFailed("Unreadable barcode data...");

                    break;
                }
            }
        }
    }

    @Override
    public void stopRead(int action) {
        Log.d("ljm2006", "stopRead()");
        switch (action){
            case ACTION_DOWN:{
                if(isReadingDownAction){
                    isReadingDownAction = false;
                    result = sb0.toString();
                    sb0.delete(0, sb0.length());
                }
                break;
            }
            case ACTION_UP:{
                if(isReadingUpAction){
                    isReadingUpAction = false;
                    result2 = sb1.toString();
                    sb1.delete(0, sb1.length());
                }
                break;
            }
        }

        Log.d("ljm2006","check Condition1 : " + isReadingDownAction + " || check Condition2 : " + isReadingUpAction);

        if(!isReadingDownAction && !isReadingUpAction){
            Log.d("ljm2006","result1 : " + result + " | result2 : " + result2);
            if(!result.equals(result2)){
                if(result.equals("")){
                    //Detected only ACTION_UP
                    if(callback != null)
                        callback.readComplete(result2);
                }else if(result2.equals("")){
                    //Detected only ACTION_DOWN
                    if(callback != null)
                        callback.readComplete(result);
                }
            }else{
//            Detected all ACTION...
                if(callback != null)
                    callback.readComplete(result);
            }
        }else{
            Log.d("ljm2006", "not completed!!");
        }
    }

    public void setOnBarcodeReaderCompleteCallback(BarcodeReaderCompleteCallback callback){
        this.callback = callback;
    }


    public interface BarcodeReaderCompleteCallback{
        void readComplete(String result);
        void readFailed(String error);
    }
}

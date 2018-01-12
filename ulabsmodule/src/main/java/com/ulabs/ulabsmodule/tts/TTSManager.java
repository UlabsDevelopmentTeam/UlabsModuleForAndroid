package com.ulabs.ulabsmodule.tts;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by OH-Biz on 2018-01-11.
 */

public class TTSManager implements ITTSManager {
    private static TTSManager ttsManager;
    private TextToSpeech tts;

    public static final int QUEUE_MODE_FLUSH = 0;
    public static final int QUEUE_MODE_ADD = 1;

    private TTSManager(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    public static TTSManager getManager(Context context){
        if(ttsManager == null){
            ttsManager = new TTSManager(context);
        }

        return ttsManager;
    }


    @Override
    public void speak(String text, float pitch, float speed, int queueMode) {
        tts.setPitch(pitch);
        tts.setSpeechRate(speed);
        switch (queueMode){
            case QUEUE_MODE_FLUSH:{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }else{
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
                break;
            }
            case QUEUE_MODE_ADD:{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
                }else{
                    tts.speak(text, TextToSpeech.QUEUE_ADD, null);
                }
                break;
            }
        }
    }

    @Override
    public void shutDown() {
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
            ttsManager = null;
        }
    }
}

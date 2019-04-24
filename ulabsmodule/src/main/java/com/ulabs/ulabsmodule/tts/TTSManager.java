package com.ulabs.ulabsmodule.tts;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by OH-Biz on 2018-01-11.
 */

public class TTSManager implements ITTSManager {
    private static TTSManager ttsManager;
    private TextToSpeech tts;
    private Context context;

    public static final int QUEUE_MODE_FLUSH = 0;
    public static final int QUEUE_MODE_ADD = 1;

    private TTSManager(final Context context) {
        this.context = context;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                   int languageResult = tts.setLanguage(Locale.KOREAN);
                    if(languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(context, "TextToSpeech language is not supported...\n we recommend using google tts", Toast.LENGTH_LONG).show();
                        Log.e("TTSManager", "TextToSpeech language is not supported...");
                        Intent intent = new Intent();
                        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        context.startActivity(intent);
                    }
                }else{
                    Log.e("TTSManager", "TextToSpeech initialization failed!!!");
                    Toast.makeText(context, "TextToSpeech initialization failed!!!", Toast.LENGTH_LONG).show();
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
        if(tts != null){
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
    }

    @Override
    public void changeLanguageLocale(final Locale locale) {
        if(tts != null){
            tts.setLanguage(locale);
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

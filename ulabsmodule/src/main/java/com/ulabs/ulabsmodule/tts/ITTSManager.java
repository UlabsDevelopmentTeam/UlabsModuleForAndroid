package com.ulabs.ulabsmodule.tts;

import java.util.Locale;

/**
 * Created by OH-Biz on 2018-01-11.
 */

public interface ITTSManager {
    void speak(String text, float pitch, float speed, int queueMode);
    void changeLanguageLocale(Locale locale);
    void shutDown();
}

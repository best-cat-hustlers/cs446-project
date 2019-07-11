package com.bestCatHustlers.sukodublitz.settings;
import android.content.SharedPreferences;

import com.bestCatHustlers.sukodublitz.GlobalSettingsInterface;

import java.util.Observable;

class MainSettingsModel extends Observable implements GlobalSettingsInterface {
    private boolean soundEnabled;
    private boolean musicEnabled;
    // Create static instance of this mModel
    private static final MainSettingsModel ourInstance = new MainSettingsModel(true,false);
    static MainSettingsModel getInstance()
    {
        return ourInstance;
    }
    private MainSettingsModel(boolean soundEnabled, boolean musicEnabled) {
        this.soundEnabled = soundEnabled;
        this.musicEnabled = musicEnabled;
    }

    void setSoundEnabled(boolean on) {
        soundEnabled = on;
    }

    void setMusicEnabled(boolean on) {
        musicEnabled = on;
    }

    public boolean getSoundEnabled(){
        return soundEnabled;
    }

    public boolean getMusicEnabled() {
        return musicEnabled;
    }

    void restoreState(SharedPreferences settings) {
        soundEnabled = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_SOUND,true);
        musicEnabled = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_MUSIC,false);
    }
}

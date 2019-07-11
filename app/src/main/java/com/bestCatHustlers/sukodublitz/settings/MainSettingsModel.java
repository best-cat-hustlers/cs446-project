package com.bestCatHustlers.sukodublitz.settings;
import android.content.SharedPreferences;

import com.bestCatHustlers.sukodublitz.GlobalSettingsInterface;

class MainSettingsModel implements GlobalSettingsInterface {
    private boolean soundEnabled;
    private boolean musicEnabled;
    // Create static instance of this mModel
    private static final MainSettingsModel ourInstance = new MainSettingsModel(true,false);
    public static GlobalSettingsInterface getInstance() {
        return ourInstance;
    }
    private MainSettingsModel(boolean soundEnabled, boolean musicEnabled) {
        this.soundEnabled = soundEnabled;
        this.musicEnabled = musicEnabled;
    }

    void setSoundEnabled(boolean newValue) {
        soundEnabled = newValue;
    }

    void setMusicEnabled(boolean newValue) {
        musicEnabled = newValue;
    }

    public boolean isSoundEnabled(){
        return soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    void restoreState(SharedPreferences settings) {
        soundEnabled = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_SOUND_ENABLED,true);
        musicEnabled = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_MUSIC_ENABLED,false);
    }
}

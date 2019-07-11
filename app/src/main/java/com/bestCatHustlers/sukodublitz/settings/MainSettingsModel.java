package com.bestCatHustlers.sukodublitz.settings;
import android.content.SharedPreferences;

import com.bestCatHustlers.sukodublitz.GlobalSettingsInterface;

import java.util.Observable;

class MainSettingsModel extends Observable implements GlobalSettingsInterface {
    private boolean sound;
    private boolean music;
    // Create static instance of this mModel
    private static final MainSettingsModel ourInstance = new MainSettingsModel(true,false);
    static MainSettingsModel getInstance()
    {
        return ourInstance;
    }
    private MainSettingsModel(boolean sound, boolean music) {
        this.sound = sound;
        this.music = music;
    }

    void setSound(boolean on) {
        sound = on;
    }

    void setMusic(boolean on) {
        music = on;
    }

    public boolean getSound(){
        return sound;
    }

    public boolean getMusic() {
        return music;
    }

    void restoreState(SharedPreferences settings) {
        sound = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_SOUND,true);
        music = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_MUSIC,false);
    }
}

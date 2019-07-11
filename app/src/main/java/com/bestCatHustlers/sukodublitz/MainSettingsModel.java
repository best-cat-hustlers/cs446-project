package com.bestCatHustlers.sukodublitz;
import android.content.SharedPreferences;

import java.util.Observable;

class MainSettingsModel extends Observable implements MainSettingsContract.Model, GlobalSettingsInterface   {
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

    @Override
    public void setSound(boolean on) {
        sound = on;
    }

    @Override
    public void setMusic(boolean on) {
        music = on;
    }

    public boolean getSound(){
        return sound;
    }

    @Override
    public boolean getMusic() {
        return music;
    }

    @Override
    public void restoreState(SharedPreferences settings) {
        sound = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_SOUND,true);
        music = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_MUSIC,false);
    }
}

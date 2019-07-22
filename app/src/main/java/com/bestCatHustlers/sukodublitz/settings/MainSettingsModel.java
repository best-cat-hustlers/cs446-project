package com.bestCatHustlers.sukodublitz.settings;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bestCatHustlers.sukodublitz.BackgroundMusicService;
import com.bestCatHustlers.sukodublitz.GlobalSettingsInterface;

import static android.content.Context.MODE_PRIVATE;

public class MainSettingsModel implements GlobalSettingsInterface {
    private boolean soundEnabled;
    private boolean musicEnabled;
    private String userName;
    // Create static instance of this mModel
    private static final MainSettingsModel ourInstance = new MainSettingsModel(true,false);
    private static BackgroundMusicService backgroundMusicService;
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
        if (newValue) {
            backgroundMusicService.onResume();
        } else {
            backgroundMusicService.onPause();
        }
    }

    public boolean isSoundEnabled(){
        return soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void restoreLastState(Context context) {
        if (backgroundMusicService == null) {
            if (context instanceof BackgroundMusicService) {
                backgroundMusicService = (BackgroundMusicService) context;
            } else {
                Log.e("BackgroundMusicService", "Does not run restoreLastState in first place");
            }
        } else {
            context = backgroundMusicService;
        }
        SharedPreferences settings = context.getSharedPreferences("globalSettings",MODE_PRIVATE);
        soundEnabled = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_SOUND_ENABLED,true);
        musicEnabled = settings.getBoolean(MainSettingsActivity.SHARE_PREF_KEY_MUSIC_ENABLED,false);
        userName = settings.getString(MainSettingsActivity.SHARE_PREF_KEY_USER_NAME,"");
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}

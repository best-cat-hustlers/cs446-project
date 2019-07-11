package com.bestCatHustlers.sukodublitz;

import android.content.SharedPreferences;

public interface MainSettingsContract {
    interface View {

    }
    interface Presenter{
        void turnSound(boolean on);
        boolean getSound();
        void turnMusic(boolean on);
        boolean getMusic();
        void viewDestroy();
    }
    interface Model {
        void setSound(boolean on);
        void setMusic(boolean on);
        boolean getSound();
        boolean getMusic();

        void restoreState(SharedPreferences settings);
    }
}

package com.bestCatHustlers.sukodublitz.settings;

import android.text.Editable;

public interface MainSettingsContract {
    interface View {

    }
    interface Presenter{
        void setSoundEnabled(boolean newValue);
        boolean isSoundEnabled();
        void setMusicEnabled(boolean newValue);
        boolean isMusicEnabled();
        void viewDestroy();
        void setUserName(String text);
        String getUserName();
    }
}

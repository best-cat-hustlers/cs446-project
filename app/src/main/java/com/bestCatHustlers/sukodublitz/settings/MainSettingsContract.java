package com.bestCatHustlers.sukodublitz.settings;

public interface MainSettingsContract {
    interface View {

    }
    interface Presenter{
        void setSoundEnabled(boolean newValue);
        boolean isSoundEnabled();
        void setMusicEnabled(boolean newValue);
        boolean isMusicEnabled();
        void viewDestroy();
    }
}

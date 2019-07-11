package com.bestCatHustlers.sukodublitz.settings;

public interface MainSettingsContract {
    interface View {

    }
    interface Presenter{
        void turnSound(boolean newValue);
        boolean getSound();
        void turnMusic(boolean newValue);
        boolean getMusic();
        void viewDestroy();
    }
}

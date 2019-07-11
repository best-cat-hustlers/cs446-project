package com.bestCatHustlers.sukodublitz.settings;

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
}

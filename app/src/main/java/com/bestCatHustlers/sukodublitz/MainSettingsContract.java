package com.bestCatHustlers.sukodublitz;

public interface MainSettingsContract {
    interface View {
        void updateSound(boolean on);
    }
    interface Presenter{
        void turnSound(boolean on);
        boolean getSound();
        void viewDestroy();
    }
    interface Model {
        void setSound(boolean on);
        boolean getSound();
    }
}

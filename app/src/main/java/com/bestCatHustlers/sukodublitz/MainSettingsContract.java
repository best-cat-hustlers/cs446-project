package com.bestCatHustlers.sukodublitz;

public interface MainSettingsContract {
    interface View {
        void updateDifficulty(int d);
    }
    interface Presenter{
        void changeDifficulty(int d);
    }
    interface Model {
        void setDifficulty(int d);
    }
}

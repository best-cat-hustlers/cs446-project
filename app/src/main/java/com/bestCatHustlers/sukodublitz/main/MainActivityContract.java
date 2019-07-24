package com.bestCatHustlers.sukodublitz.main;

interface MainActivityContract {
    interface View {

    }
    interface Presenter{
        void goToSinglePlayer(android.view.View view);
        void goToMultiplayer(android.view.View view);
    }
    interface Model {

    }
}

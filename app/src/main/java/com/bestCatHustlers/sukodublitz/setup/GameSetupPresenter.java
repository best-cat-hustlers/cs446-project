package com.bestCatHustlers.sukodublitz.setup;

import android.support.v7.app.AppCompatActivity;

public class GameSetupPresenter implements GameSetupContract.Presenter {
    private AppCompatActivity gameSetupActivity;

    public GameSetupPresenter(AppCompatActivity activity) {
        gameSetupActivity = activity;
    }

}

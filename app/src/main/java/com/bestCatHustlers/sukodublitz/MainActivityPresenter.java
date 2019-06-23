package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


class MainActivityPresenter {
    private static final int GET_DIFFICULTY = 0;
    private AppCompatActivity mainActivity;
    public MainActivityPresenter(AppCompatActivity activity) {
        mainActivity = activity;
    }

    protected void goToSinglePlayer(View view) {
        Intent intent = new Intent(mainActivity, GameSetupActivity.class);
        mainActivity.startActivity(intent);
    }
    protected void goToMultiplayer(View view) {
        Intent intent = new Intent(mainActivity, MultiplayerMenuActivity.class);
        mainActivity.startActivity(intent);
    }
    protected void gotoMainSettings(View view) {
        Intent intent = new Intent(mainActivity, MainSettingsActivity.class);
        mainActivity.startActivityForResult(intent, GET_DIFFICULTY);

    }
}

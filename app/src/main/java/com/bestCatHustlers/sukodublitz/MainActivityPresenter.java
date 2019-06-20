package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


class MainActivityPresenter {
    AppCompatActivity mainActivity;
    public MainActivityPresenter(AppCompatActivity activity) {
        mainActivity = activity;
    }

    protected void goToSinglePlayer(View view) {
        Intent intent = new Intent(mainActivity, com.bestCatHustlers.sukodublitz.GameSetupActivity.class);

        mainActivity.startActivity(intent);
    }

    protected void goToMultiplayer(View view) {
        Intent intent = new Intent(mainActivity, MultiplayerMenuActivity.class);

        mainActivity.startActivity(intent);
    }

    protected void goToMainSettings(View view) {
        Intent intent = new Intent(mainActivity, com.bestCatHustlers.sukodublitz.MainSettingsActivity.class);

        mainActivity.startActivity(intent);
    }
}

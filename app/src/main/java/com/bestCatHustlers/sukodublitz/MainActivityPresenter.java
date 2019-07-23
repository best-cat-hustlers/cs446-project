package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuActivity;
import com.bestCatHustlers.sukodublitz.setup.GameSetupActivity;


class MainActivityPresenter {

    private AppCompatActivity mainActivity;
    MainActivityPresenter(AppCompatActivity activity) {
        mainActivity = activity;
    }

    void goToSinglePlayer(View view) {
        Intent intent = new Intent(mainActivity, GameSetupActivity.class);
        mainActivity.startActivity(intent);
    }
    void goToMultiplayer(View view) {
        Intent intent = new Intent(mainActivity, MultiplayerMenuActivity.class);
        mainActivity.startActivity(intent);
    }
}

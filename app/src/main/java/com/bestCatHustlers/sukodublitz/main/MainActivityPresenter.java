package com.bestCatHustlers.sukodublitz.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuActivity;
import com.bestCatHustlers.sukodublitz.setup.GameSetupActivity;


class MainActivityPresenter implements MainActivityContract.Presenter {

    private AppCompatActivity mainActivity;
    private MainActivityContract.Model model;

    MainActivityPresenter(AppCompatActivity activity) {
        mainActivity = activity;
        model = new MainActivityModel();
    }

    public void goToSinglePlayer(View view) {
        Intent intent = new Intent(mainActivity, GameSetupActivity.class);
        mainActivity.startActivity(intent);
    }
    public void goToMultiplayer(View view) {
        Intent intent = new Intent(mainActivity, MultiplayerMenuActivity.class);
        mainActivity.startActivity(intent);
    }
}

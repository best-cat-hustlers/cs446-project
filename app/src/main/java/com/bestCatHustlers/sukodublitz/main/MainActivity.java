package com.bestCatHustlers.sukodublitz.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bestCatHustlers.sukodublitz.BackgroundMusicService;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.settings.MainSettingsActivity;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{
    MainActivityContract.Presenter mainPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mainPresenter = new MainActivityPresenter(this);
        Intent backgroundMusicService = new Intent(this, BackgroundMusicService.class);
        startService(backgroundMusicService);
    }

    public void clickSinglePlayer(View view) {
        mainPresenter.goToSinglePlayer(view);
    }

    public void clickMultiplayer(View view) {
        mainPresenter.goToMultiplayer(view);
    }

    public void clickSettings(View view) {
        Intent intent = new Intent(this, MainSettingsActivity.class);
        startActivity(intent);
    }
}

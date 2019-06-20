package com.bestCatHustlers.sukodublitz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity{
    MainActivityPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mainPresenter = new MainActivityPresenter(this);
    }

    public void clickSinglePlayer(View view) {
        mainPresenter.goToSinglePlayer(view);
    }

    public void clickMultiplayer(View view) {
        mainPresenter.goToMultiplayer(view);
    }

    public void clickSettings(View view) {
        mainPresenter.goToMainSettings(view);
    }
}

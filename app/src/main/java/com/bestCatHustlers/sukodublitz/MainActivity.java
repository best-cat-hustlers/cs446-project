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
    protected void clickSinglePlayer(View view) {
        mainPresenter.goToSinglePlayer(view);
    }
    protected void clickMultiplayer(View view) {
        mainPresenter.goToMultiplayer(view);
    }
    protected void clickSettings(View view) {
        mainPresenter.gotoMainSettings(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Need to somehow store the data (difficulty)
    }
}

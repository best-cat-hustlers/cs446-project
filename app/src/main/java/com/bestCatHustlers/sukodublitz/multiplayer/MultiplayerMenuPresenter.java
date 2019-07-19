package com.bestCatHustlers.sukodublitz.multiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bestCatHustlers.sukodublitz.results.ResultsContract;

public class MultiplayerMenuPresenter implements MultiplayerMenuContract.Presenter {

    private MultiplayerMenuContract.View view;

    public static final String EXTRAS_KEY_IS_MULTI = "isMultiplayer";

    public MultiplayerMenuPresenter(MultiplayerMenuContract.View view) {
        this.view = view;
    }

    @Override
    public void prepareOpenGameSetup(Intent intent) {
        intent.putExtra(EXTRAS_KEY_IS_MULTI,true);
    }
}

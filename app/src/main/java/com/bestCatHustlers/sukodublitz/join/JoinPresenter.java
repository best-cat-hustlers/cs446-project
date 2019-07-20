package com.bestCatHustlers.sukodublitz.join;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.game.GameContract;
import com.bestCatHustlers.sukodublitz.game.GamePresenter;

public class JoinPresenter implements JoinContract.Presenter {

    public static final String EXTRAS_KEY_IS_HOST = "isHost";

    private JoinContract.View view;

    public JoinPresenter(JoinContract.View view) {
        this.view = view;
    }

    @Override
    public void handleViewCreated() {
        view.checkBluetoothSupport();
    }

    @Override
    public void handleViewStarted() {
        view.setupBluetoothSession();
    }

    @Override
    public void handleViewStopped() {
        view.stopBluetoothService();
    }

    @Override
    public void handleOnBackPressed() {
        view.stopBluetoothService();
    }

    @Override
    public void handleFindDevicePressed() {
        view.findDevices();
    }

    @Override
    public void prepareOpenLobbyActivity(Intent intent) {
        intent.putExtra(EXTRAS_KEY_IS_HOST, false);
    }
}

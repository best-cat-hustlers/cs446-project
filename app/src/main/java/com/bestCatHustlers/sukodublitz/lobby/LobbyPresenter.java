package com.bestCatHustlers.sukodublitz.lobby;

import android.graphics.Paint;
import android.os.Bundle;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.ExtrasKeys;
import com.bestCatHustlers.sukodublitz.GameSetupActivity;
import com.bestCatHustlers.sukodublitz.GameSetupPresenter;

public class LobbyPresenter implements LobbyContract.Presenter {
    //region Properties

    private LobbyContract.View view;
    private BoardGame model;

    private boolean isHost;
    private boolean isMultiplayer;

    private Constants constants;

    private class Constants {

    }

    //endregion

    //region LifeCycle

    public LobbyPresenter(LobbyContract.View view, Bundle extras) {
        this.view = view;

        // TODO: Setup the model from the extras sent in from the previous activity.
        model = new BoardGame();

        constants = new Constants();

        configureWithSettings(extras);
    }

    //endregion

    //region Contract

    @Override
    public void handleViewCreated() {

    }

    @Override
    public void handleViewStarted() {

    }

    @Override
    public void handleViewStopped() {

    }

    @Override
    public void handleViewDestroyed() {

    }

    @Override
    public void handleBluetoothMessageReceived(byte[] message) {

    }

    @Override
    public void handleStartGamePressed() {

    }

    //endregion

    //region Private

    private void configureWithSettings(Bundle extras) {
        isHost = extras.getBoolean(ExtrasKeys.IS_HOST);

        if (isHost) {
            model = extras.getParcelable(ExtrasKeys.BOARD_GAME);
        }
    }

    //endregion
}

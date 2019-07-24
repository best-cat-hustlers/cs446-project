package com.bestCatHustlers.sukodublitz.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.BoardGameSerializedObject;
import com.bestCatHustlers.sukodublitz.ExtrasKeys;
import com.bestCatHustlers.sukodublitz.GameSettings;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothMessage;
import com.bestCatHustlers.sukodublitz.settings.MainSettingsModel;
import com.bestCatHustlers.sukodublitz.utils.SerializableUtils;

public class LobbyPresenter implements LobbyContract.Presenter {
    //region Properties

    private LobbyContract.View view;
    private BoardGame model;
    private GameSettings gameSettings;

    private String connectedDeviceName;
    private boolean isHost;

    private class Constants {
        private class BluetoothTags {
            static final String addRequest = "pleaseAddMeIAmLonely";
            static final String propagateBoardGame = "propagateBoardGame";
            static final String gameSettings = "gameSettings";
            static final String gameStart = "gameStart";
        }

        static final int discoveryDuration = 300;
    }

    //endregion

    //region LifeCycle

    public LobbyPresenter(LobbyContract.View view, Bundle extras) {
        this.view = view;

        model = new BoardGame();

        configureWithSettings(extras);
    }

    //endregion

    //region Contract

    @Override
    public void handleViewCreated() {
        if (isHost) {
            view.openDiscoverableAlert(Constants.discoveryDuration);
        } else {
            view.setStartGameVisibility(View.INVISIBLE);
        }
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
    public void handleBluetoothMessageReceived(Message message) {
        Log.d("LOBBY_P_BT_HANDLER", "what:" + message.what);

        switch (message.what) {
            case BluetoothConstants.MESSAGE_STATE_CHANGE:
                handleBluetoothStateChangeMessage(message.arg1);
                break;
            case BluetoothConstants.MESSAGE_DEVICE_NAME:
                connectedDeviceName = message.getData().getString(BluetoothConstants.DEVICE_NAME);
                break;
            case BluetoothConstants.MESSAGE_READ:
                handleBluetoothMessageRead(message);
                break;
            case BluetoothConstants.MESSAGE_WRITE:
                handleBluetoothMessageSent(message);
                break;
        }
    }

    @Override
    public void handleStartGamePressed() {
        if (isHost) {
            BluetoothMessage startGameMessage = new BluetoothMessage(Constants.BluetoothTags.gameStart, true);

            view.sendBluetoothMessage(startGameMessage.serialized());
        }
    }

    @Override
    public void prepareOpenGameActivity(Intent intent) {
        intent.putExtra(ExtrasKeys.IS_HOST, isHost);
        intent.putExtra(ExtrasKeys.IS_MULTIPLAYER, true);

        if (gameSettings != null)  {
            intent.putExtra(ExtrasKeys.SHOULD_SHOW_POINTS, gameSettings.shouldShowPoints);
            intent.putExtra(ExtrasKeys.SHOULD_SHOW_TIMER, gameSettings.shouldShowTimer);
            intent.putExtra(ExtrasKeys.SHOULD_USE_PENALTY, gameSettings.shouldUsePenalty);
        }

        if (model != null) {
            intent.putExtra(ExtrasKeys.BOARD_GAME, (Parcelable) model);
        }
    }

    @Override
    public void handleOnBluetoothServiceConnected() {
        if (isHost) {
            view.hostBluetoothService();
        }
    }

    //endregion

    //region Private

    private void configureWithSettings(Bundle extras) {
        isHost = extras.getBoolean(ExtrasKeys.IS_HOST);

        if (isHost) {
            model = extras.getParcelable(ExtrasKeys.BOARD_GAME);

            gameSettings = new GameSettings();
            gameSettings.shouldShowPoints = extras.getBoolean(ExtrasKeys.SHOULD_SHOW_POINTS);
            gameSettings.shouldShowTimer = extras.getBoolean(ExtrasKeys.SHOULD_SHOW_TIMER);
            gameSettings.shouldUsePenalty = extras.getBoolean(ExtrasKeys.SHOULD_USE_PENALTY);
        }
    }

    private void handleBluetoothStateChangeMessage(int state) {
        if (state == BluetoothConstants.STATE_CONNECTED) {
            view.setStatusText("Connected to " + connectedDeviceName);

            handleDeviceConnected();
        }
    }

    private void handleBluetoothMessageRead(Message rawMessage) {
        byte[] buffer = (byte[]) rawMessage.obj;
        Object object = SerializableUtils.deserialize(buffer);

        if (object instanceof BluetoothMessage) {
            BluetoothMessage message = (BluetoothMessage) object;

            Log.d("LOBBY_BT_READ", "tag: " + message.tag + " payload class name: " + message.payload.getClass().getName());

            switch (message.tag) {
                case Constants.BluetoothTags.gameSettings:
                    gameSettings = (GameSettings) message.payload;

                    requestToBeAddedToGame(Player.Team.RED);
                    break;
                case Constants.BluetoothTags.gameStart:
                    view.openGameActivity();
                    break;
                case Constants.BluetoothTags.addRequest:
                    if (isHost) {
                        Player playerToAdd = (Player) message.payload;
                        model.addPlayer(playerToAdd.getId(), playerToAdd.getTeam());

                        propagateBoardGame();
                    }
                    break;
                case Constants.BluetoothTags.propagateBoardGame:
                    BoardGameSerializedObject serializedBoardGame = (BoardGameSerializedObject) message.payload;

                    model.syncWithSerializedObject(serializedBoardGame);
                    break;
            }
        }

    }

    private void handleBluetoothMessageSent(Message rawMessage) {
        byte[] buffer = (byte[]) rawMessage.obj;
        Object object = SerializableUtils.deserialize(buffer);

        if (object instanceof  BluetoothMessage) {
            BluetoothMessage message = (BluetoothMessage) object;

            Log.d("LOBBY_BT_SENT", "tag: " + message.tag + " payload class name: " + message.payload.getClass().getName());

            if (message.tag.equals(Constants.BluetoothTags.gameStart)) {
                view.openGameActivity();
            }

        }
    }

    private void handleDeviceConnected() {
        if (isHost) {
            BluetoothMessage settingsMessage = new BluetoothMessage(Constants.BluetoothTags.gameSettings, gameSettings);

            view.sendBluetoothMessage(settingsMessage.serialized());
        }
    }

    private void requestToBeAddedToGame(Player.Team team) {
        Player player = new Player(MainSettingsModel.getInstance().getUserID(), team);
        BluetoothMessage addRequestMessage = new BluetoothMessage(Constants.BluetoothTags.addRequest, player);

        view.sendBluetoothMessage(addRequestMessage.serialized());
    }

    private void propagateBoardGame() {
        if (!isHost) return;

        BoardGameSerializedObject serializedBoardGame = model.getSerializedObject();
        BluetoothMessage propagateBoardGameMessage = new BluetoothMessage(Constants.BluetoothTags.propagateBoardGame, serializedBoardGame);

        view.sendBluetoothMessage(propagateBoardGameMessage.serialized());
    }

    //endregion
}

package com.bestCatHustlers.sukodublitz.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.ExtrasKeys;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothMessage;
import com.bestCatHustlers.sukodublitz.settings.MainSettingsModel;
import com.bestCatHustlers.sukodublitz.utils.SerializableUtils;

public class LobbyPresenter implements LobbyContract.Presenter {
    //region Properties

    private LobbyContract.View view;
    private BoardGame model;
    private String connectedDeviceName;

    private boolean isHost;
    private boolean shouldShowPoints;
    private boolean shouldShowTimer;
    private boolean shouldUsePenalty;

    private class Constants {
        static final String startGameMessage = "startGame";
        static final int discoveryDuration = 300;
    }

    //endregion

    //region LifeCycle

    public LobbyPresenter(LobbyContract.View view, Bundle extras) {
        this.view = view;

        // TODO: Setup the model from the extras sent in from the previous activity.
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
            case BluetoothConstants
                    .MESSAGE_STATE_CHANGE:
                Log.d("LOBBY_P_BT_HANDLER", "state");
                handleBluetoothStateChangeMessage(message.arg1);
                break;
            case BluetoothConstants.MESSAGE_DEVICE_NAME:
                Log.d("LOBBY_P_BT_HANDLER", "name");
                connectedDeviceName = message.getData().getString(BluetoothConstants.DEVICE_NAME);
                break;
            case BluetoothConstants.MESSAGE_READ:
                Log.d("LOBBY_P_BT_HANDLER", "read");
                handleBluetoothMessageRead(message);
                break;
            case BluetoothConstants.MESSAGE_WRITE:
                Log.d("LOBBY_P_BT_HANDLER", "write");
                handleBluetoothMessageSent(message);
                break;
        }
    }

    @Override
    public void handleStartGamePressed() {
        BluetoothMessage startGameMessage = new BluetoothMessage(MainSettingsModel.getInstance().getUserID(), Constants.startGameMessage);

        view.sendBluetoothMessage(startGameMessage.serialized());
    }

    @Override
    public void prepareOpenGameActivity(Intent intent) {
        intent.putExtra(ExtrasKeys.IS_HOST, isHost);
        intent.putExtra(ExtrasKeys.IS_MULTIPLAYER, true);
        intent.putExtra(ExtrasKeys.SHOULD_SHOW_POINTS, shouldShowPoints);
        intent.putExtra(ExtrasKeys.SHOULD_SHOW_TIMER, ExtrasKeys.SHOULD_SHOW_TIMER);
        intent.putExtra(ExtrasKeys.SHOULD_USE_PENALTY, shouldUsePenalty);
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
            shouldShowPoints = extras.getBoolean(ExtrasKeys.SHOULD_SHOW_POINTS);
            shouldShowTimer = extras.getBoolean(ExtrasKeys.SHOULD_SHOW_TIMER);
            shouldUsePenalty = extras.getBoolean(ExtrasKeys.SHOULD_USE_PENALTY);
        }
    }

    private void handleBluetoothStateChangeMessage(int state) {
        if (state == BluetoothConstants.STATE_CONNECTED) {
            view.setStatusText("Connected to" + connectedDeviceName);
        }
    }

    private void handleBluetoothMessageRead(Message rawMessage) {
        byte[] buffer = (byte[]) rawMessage.obj;
        Object object = SerializableUtils.deserialize(buffer);

        if (object instanceof BluetoothMessage) {
            BluetoothMessage message = (BluetoothMessage) object;

            Log.d("BT_READ", "tag: " + message.tag + " payload class name: " + message.payload.getClass().getName());
        }

    }

    private void handleBluetoothMessageSent(Message rawMessage) {
        byte[] buffer = (byte[]) rawMessage.obj;
        Object object = SerializableUtils.deserialize(buffer);

        if (object instanceof  BluetoothMessage) {
            BluetoothMessage message = (BluetoothMessage) object;

            Log.d("BT_SENT", "tag: " + message.tag + " payload class name: " + message.payload.getClass().getName());
        }
    }

    //endregion
}

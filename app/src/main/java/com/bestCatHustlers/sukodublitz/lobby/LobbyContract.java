package com.bestCatHustlers.sukodublitz.lobby;

import android.content.Intent;
import android.os.Message;

public class LobbyContract {
    interface View {
        void sendBluetoothMessage(byte[] message);

        void setStatusText(String text);

        void openGameActivity();

        void hostBluetoothService();

        void openDiscoverableAlert(int durationSeconds);

        void setStartGameVisibility(int visibility);

        void setToggleTeamButton(int color, String text);
    }

    interface Presenter {
        void handleViewCreated();

        void handleViewStarted();

        void handleViewStopped();

        void handleViewDestroyed();

        void handleBluetoothMessageReceived(Message message);

        void handleStartGamePressed();

        void handleToggleTeamPressed();

        void prepareOpenGameActivity(Intent intent);

        void handleOnBluetoothServiceConnected();
    }
}

package com.bestCatHustlers.sukodublitz.lobby;

public class LobbyContract {
    interface View {
        void sendBluetoothMessage(byte[] message);
    }

    interface Presenter {
        void handleViewCreated();

        void handleViewStarted();

        void handleViewStopped();

        void handleViewDestroyed();

        void handleBluetoothMessageReceived(byte[] message);

        void handleStartGamePressed();
    }
}

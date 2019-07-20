package com.bestCatHustlers.sukodublitz.join;

import android.content.Intent;

public class JoinContract {
    interface View {
        void checkBluetoothSupport();

        void setupBluetoothSession();

        void stopBluetoothService();

        void findDevices();
    }

    interface Presenter {
        void handleViewCreated();

        void handleViewStarted();

        void handleViewStopped();

        void handleOnBackPressed();

        void handleFindDevicePressed();

        void prepareOpenLobbyActivity(Intent intent);
    }
}

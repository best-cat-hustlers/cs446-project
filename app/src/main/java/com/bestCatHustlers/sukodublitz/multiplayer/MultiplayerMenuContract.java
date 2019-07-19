package com.bestCatHustlers.sukodublitz.multiplayer;

import android.content.Intent;

public class MultiplayerMenuContract {
    interface View {

    }

    interface Presenter {
        void prepareOpenGameSetup(Intent intent);
    }
}

package com.bestCatHustlers.sukodublitz.multiplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bestCatHustlers.sukodublitz.GameSetupActivity;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.join.JoinActivity;
import com.bestCatHustlers.sukodublitz.lobby.LobbyActivity;

public class MultiplayerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer_menu);
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }

    public void openGameSetup(View view) {
        Intent intent = new Intent(this, GameSetupActivity.class);
//        Intent intent = new Intent(this, LobbyActivity.class); // for testing

        startActivity(intent);
    }

    public void openJoinActivity(View view) {
        Intent intent = new Intent(this, JoinActivity.class);

        startActivity(intent);
    }
}

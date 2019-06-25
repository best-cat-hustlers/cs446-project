package com.bestCatHustlers.sukodublitz.join;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bestCatHustlers.sukodublitz.lobby.LobbyActivity;
import com.bestCatHustlers.sukodublitz.R;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }

    public void openLobbyActivity(View view) {
        Intent intent = new Intent(this, LobbyActivity.class);

        startActivity(intent);
    }
}

package com.bestCatHustlers.sukodublitz.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.game.GameActivity;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }

    public void openGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);

        startActivity(intent);
    }

}

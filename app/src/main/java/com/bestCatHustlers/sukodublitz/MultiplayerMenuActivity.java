package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MultiplayerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void openGameSetup(View view) {
        Intent intent = new Intent(this, GameSetupActivity.class);

        startActivity(intent);
    }

    public void openJoinActivity(View view) {
        Intent intent = new Intent(this, JoinActivity.class);

        startActivity(intent);
    }
}

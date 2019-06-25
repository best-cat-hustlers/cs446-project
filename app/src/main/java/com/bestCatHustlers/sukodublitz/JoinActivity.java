package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);
    }

    public void openLobbyActivity(View view) {
        Intent intent = new Intent(this, LobbyActivity.class);

        startActivity(intent);
    }
}

package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bestCatHustlers.sukodublitz.game.GameActivity;

public class GameSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
    }
    protected void clickGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }
}

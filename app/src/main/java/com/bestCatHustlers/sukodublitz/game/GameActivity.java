package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.ResultsActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
    }

    public void openResultsActivity(View view) {
        Intent intent = new Intent(this, ResultsActivity.class);

        startActivity(intent);
    }
}
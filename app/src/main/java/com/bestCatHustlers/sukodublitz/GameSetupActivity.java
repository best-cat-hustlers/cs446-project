package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.bestCatHustlers.sukodublitz.game.GameActivity;

public class GameSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        // Default radio button difficulty to 1
        ((RadioButton) findViewById(R.id.difficulty1)).toggle();
    }

    public void clickGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }

    public void onChangeAIDifficulty(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.difficulty1:
                if (checked)
                    // set some global variable, then send to model when start game is clicked
                    break;
            case R.id.difficulty2:
                if (checked)
                    break;
            case R.id.difficulty3:
                if (checked)
                    break;
            case R.id.difficulty4:
                if (checked)
                    break;
            case R.id.difficulty5:
                if (checked)
                    break;
        }
    }

    public void onBackPressed(View view){
        super.onBackPressed();
    }
}

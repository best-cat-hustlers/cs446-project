package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.bestCatHustlers.sukodublitz.game.GameActivity;

public class GameSetupActivity extends AppCompatActivity {

    RadioButton button1;
    RadioButton button2;
    RadioButton button3;
    RadioButton button4;
    RadioButton button5;

    private boolean showPoints;
    private boolean showTimer;
    private boolean penalty;
    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        button1 = findViewById(R.id.difficulty1);
        button2 = findViewById(R.id.difficulty2);
        button3 = findViewById(R.id.difficulty3);
        button4 = findViewById(R.id.difficulty4);
        button5 = findViewById(R.id.difficulty5);

        // Default radio button difficulty to 1
        button1.toggle();
        onChangeAIDifficulty(button1);
        // Default settings
        showPoints = true;
        showTimer = true;
        penalty = true;
        difficulty = 1;
    }

    public void clickGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
    }
    
    public void onCheckShowPoints(View view){}
    public void onCheckShowTimer(View view){}
    public void onCheckPenalty(View view){}

    public void resetRadioButtonTextColor() {
        button1.setTextColor(getResources().getColor(R.color.secondaryColor));
        button2.setTextColor(getResources().getColor(R.color.secondaryColor));
        button3.setTextColor(getResources().getColor(R.color.secondaryColor));
        button4.setTextColor(getResources().getColor(R.color.secondaryColor));
        button5.setTextColor(getResources().getColor(R.color.secondaryColor));
    }

    public void onChangeAIDifficulty(View view) {
        RadioButton selectedRadioButton = (RadioButton) view;
        boolean checked = selectedRadioButton.isChecked();

        resetRadioButtonTextColor();
        selectedRadioButton.setTextColor(getResources().getColor(R.color.white));

        switch(view.getId()) {
            case R.id.difficulty1:
                if (checked)
                    // set some global variable, then send to model when start game is clicked
                    difficulty = 1;
                    break;
            case R.id.difficulty2:
                if (checked)
                    difficulty = 2;
                    break;
            case R.id.difficulty3:
                if (checked)
                    difficulty = 3;
                    break;
            case R.id.difficulty4:
                if (checked)
                    difficulty = 4;
                    break;
            case R.id.difficulty5:
                if (checked)
                    difficulty = 5;
                    break;
        }
    }

    public void onBackPressed(View view){
        super.onBackPressed();
    }
}

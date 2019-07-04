package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;

import com.bestCatHustlers.sukodublitz.game.GameActivity;

public class GameSetupActivity extends AppCompatActivity {

    public static final String SHOW_POINTS = "showPoints";
    public static final String SHOW_TIMER = "showTimer";
    public static final String PENALTY_ON = "penaltyOn";
    public static final String AI_DIFFICULTY = "aiDifficulty";
    RadioButton button1;
    RadioButton button2;
    RadioButton button3;
    RadioButton button4;
    RadioButton button5;

    // Default settings
    private boolean showPoints = true;
    private boolean showTimer = true;
    private boolean penaltyOn = true;
    private int aiDifficulty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        button1 = findViewById(R.id.difficulty1);
        button2 = findViewById(R.id.difficulty2);
        button3 = findViewById(R.id.difficulty3);
        button4 = findViewById(R.id.difficulty4);
        button5 = findViewById(R.id.difficulty5);

        // Default radio button aiDifficulty to 1
        button1.toggle();
        onChangeAIDifficulty(button1);

    }

    public void clickGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(SHOW_POINTS, showPoints);
        intent.putExtra(SHOW_TIMER, showTimer);
        intent.putExtra(PENALTY_ON, penaltyOn);
        intent.putExtra(AI_DIFFICULTY, aiDifficulty);
        this.startActivity(intent);
    }

    public void onCheckShowPoints(View view){
        Switch sw = (Switch) view;
        showPoints = sw.isChecked();
    }
    public void onCheckShowTimer(View view){
        Switch sw = (Switch) view;
        showTimer = sw.isChecked();
    }
    public void onCheckPenalty(View view){
        Switch sw = (Switch) view;
        penaltyOn = sw.isChecked();
    }

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
                    aiDifficulty = 1;
                    break;
            case R.id.difficulty2:
                if (checked)
                    aiDifficulty = 2;
                    break;
            case R.id.difficulty3:
                if (checked)
                    aiDifficulty = 3;
                    break;
            case R.id.difficulty4:
                if (checked)
                    aiDifficulty = 4;
                    break;
            case R.id.difficulty5:
                if (checked)
                    aiDifficulty = 5;
                    break;
        }
    }

    public void onBackPressed(View view){
        super.onBackPressed();
    }
}

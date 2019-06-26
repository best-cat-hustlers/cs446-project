package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.bestCatHustlers.sukodublitz.game.GameActivity;

public class GameSetupActivity extends AppCompatActivity {

    RadioButton button1;
    RadioButton button2;
    RadioButton button3;
    RadioButton button4;
    RadioButton button5;

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
    }

    public void clickGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        this.startActivity(intent);
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

package com.bestCatHustlers.sukodublitz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Switch;

import com.bestCatHustlers.sukodublitz.game.GameActivity;
import com.bestCatHustlers.sukodublitz.lobby.LobbyActivity;
import com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuActivity;

import static com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuPresenter.EXTRAS_KEY_IS_MULTI;

public class GameSetupActivity extends AppCompatActivity {

    public static final String EXTRAS_KEY_IS_HOST = "isHost";
    public static final String EXTRAS_KEY_SHOW_POINTS = "showPoints";
    public static final String EXTRAS_KEY_SHOW_TIMER = "showTimer";
    public static final String EXTRAS_KEY_PENALTY_ON = "penaltyOn";
    public static final String EXTRAS_KEY_AI_DIFFICULTY = "aiDifficulty";
    private static final int topToBottomMarginRatio = 4;
    RadioButton aiDifficulty1Button;
    RadioButton aiDifficulty2Button;
    RadioButton aiDifficulty3Button;
    RadioButton aiDifficulty4Button;
    RadioButton aiDifficulty5Button;

    // Default settings
    private boolean showPoints = true;
    private boolean showTimer = true;
    private boolean penaltyOn = true;
    private boolean isMultiplayer;
    private int aiDifficulty = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        // Determine if it's in multiplayer mode
        isMultiplayer = getIntent().getBooleanExtra(MultiplayerMenuActivity.EXTRAS_KEY_IS_MULTI, false);

        if (isMultiplayer) {
            // Hide AI difficulty options
            View aiGroupView = findViewById(R.id.AI_difficulty);
            View aiTitle = findViewById(R.id.textView);
            View bottomView = findViewById(R.id.penalty_switch);
            aiGroupView.setVisibility(View.GONE);
            aiTitle.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) bottomView.getLayoutParams();
            params.bottomMargin = params.topMargin * topToBottomMarginRatio;
        } else {
            // Single player mode
            aiDifficulty1Button = findViewById(R.id.difficulty1);
            aiDifficulty2Button = findViewById(R.id.difficulty2);
            aiDifficulty3Button = findViewById(R.id.difficulty3);
            aiDifficulty4Button = findViewById(R.id.difficulty4);
            aiDifficulty5Button = findViewById(R.id.difficulty5);

            // Default radio button aiDifficulty to 1
            aiDifficulty1Button.toggle();
            onChangeAIDifficulty(aiDifficulty1Button);
        }

    }

    public void clickGame(View view) {
        if (isMultiplayer) {
            Intent intent = new Intent(this, LobbyActivity.class);
            intent.putExtra(EXTRAS_KEY_IS_HOST, true);
            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(EXTRAS_KEY_SHOW_POINTS, showPoints);
            intent.putExtra(EXTRAS_KEY_SHOW_TIMER, showTimer);
            intent.putExtra(EXTRAS_KEY_PENALTY_ON, penaltyOn);
            intent.putExtra(EXTRAS_KEY_AI_DIFFICULTY, aiDifficulty);
            this.startActivity(intent);
        }
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
        aiDifficulty1Button.setTextColor(getResources().getColor(R.color.secondaryColor));
        aiDifficulty2Button.setTextColor(getResources().getColor(R.color.secondaryColor));
        aiDifficulty3Button.setTextColor(getResources().getColor(R.color.secondaryColor));
        aiDifficulty4Button.setTextColor(getResources().getColor(R.color.secondaryColor));
        aiDifficulty5Button.setTextColor(getResources().getColor(R.color.secondaryColor));
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

package com.bestCatHustlers.sukodublitz.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.game.GameActivity;
import com.bestCatHustlers.sukodublitz.lobby.LobbyActivity;
import com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuPresenter;

public class GameSetupActivity extends AppCompatActivity implements GameSetupContract.View{

    private static final int topToBottomMarginRatio = 4;
    Button nextPageButton;


    GameSetupContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        presenter = new GameSetupPresenter(this);
        // Determine if it's in multiplayer mode
        presenter.setMultiplayerMode(getIntent().getBooleanExtra(MultiplayerMenuPresenter.EXTRAS_KEY_IS_MULTI, false));

        nextPageButton = findViewById(R.id.button_start_game);

        if (presenter.isMultiplayerMode()) {
            hideAIDifficultyOptions();
            nextPageButton.setText("Go to Lobby");
        } else {
            presenter.initAiButtons();
            // Toggle 1
            onChangeAIDifficulty(findViewById(R.id.difficulty1));
        }
    }
    public void hideAIDifficultyOptions() {
        // Hide AI difficulty options
        View aiGroupView = findViewById(R.id.AI_difficulty);
        View aiTitle = findViewById(R.id.textView);
        View bottomView = findViewById(R.id.penalty_switch);
        aiGroupView.setVisibility(View.GONE);
        aiTitle.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) bottomView.getLayoutParams();
        params.bottomMargin = params.topMargin * topToBottomMarginRatio;
    }


    public void clickGame(View view) {
        if (presenter.isMultiplayerMode()) {
            Intent intent = new Intent(this, LobbyActivity.class);
            presenter.prepareLobbyExtras(intent);

            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            presenter.prepareGameExtras(intent);
            this.startActivity(intent);
        }
    }

    public void onCheckShowPoints(View view){
        Switch sw = (Switch) view;
        presenter.setShowPoints(sw.isChecked());
    }
    public void onCheckShowTimer(View view){
        Switch sw = (Switch) view;
        presenter.setShowTimer(sw.isChecked());
    }
    public void onCheckPenalty(View view){
        Switch sw = (Switch) view;
        presenter.setPenaltyOn(sw.isChecked());
    }

    public void onChangeAIDifficulty(View view) {
        presenter.changeAiDifficulty(view);
    }

    public void onBackPressed(View view){
        super.onBackPressed();
    }

}

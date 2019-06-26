package com.bestCatHustlers.sukodublitz.game;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.bestCatHustlers.sukodublitz.MainActivity;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.results.ResultsActivity;

public class GameActivity extends AppCompatActivity implements GameContract.View, GameBoardView.Delegate {
    //region Properties

    private MediaPlayer lowPop;
    private MediaPlayer middlePop;
    private MediaPlayer highPop;

    private TextView playerScore1TextView;
    private TextView playerScore2TextView;
    private Chronometer chronometer;
    private GameBoardView boardView;
    private TextView[] numberEntryButtons;

    private GameContract.Presenter presenter;

    private Constants constants;

    private class Constants {
        final float numberEntrySelectedAlpha = 0.6f;
        final float numberEntryDefaultAlpha = 0.9f;
    }

    //endregion

    //region LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_game);

        setupSounds();

        playerScore1TextView = findViewById(R.id.playerScore1TextView);
        playerScore2TextView = findViewById(R.id.playerScore2TextView);

        chronometer = findViewById(R.id.chronometer);
        chronometer.start();

        numberEntryButtons = new TextView[9];
        numberEntryButtons[0] = findViewById(R.id.numberEntry1);
        numberEntryButtons[1] = findViewById(R.id.numberEntry2);
        numberEntryButtons[2] = findViewById(R.id.numberEntry3);
        numberEntryButtons[3] = findViewById(R.id.numberEntry4);
        numberEntryButtons[4] = findViewById(R.id.numberEntry5);
        numberEntryButtons[5] = findViewById(R.id.numberEntry6);
        numberEntryButtons[6] = findViewById(R.id.numberEntry7);
        numberEntryButtons[7] = findViewById(R.id.numberEntry8);
        numberEntryButtons[8] = findViewById(R.id.numberEntry9);

        constants = new Constants();

        boardView = (GameBoardView) findViewById(R.id.boardLayout);
        boardView.delegate = this;

        // TODO: Get model from intent extras.
        presenter = new GamePresenter(this, null);

        presenter.handleViewCreated();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void onBackPressed(View view) {
        presenter.handleOnBackPressed();
    }

    public void openResultsActivity(View view) {
        Intent intent = new Intent(this, ResultsActivity.class);

        presenter.prepareOpenResultsActivity(intent);

        startActivity(intent);
    }

    public void onClickNumberEntry(View view) {
        TextView button = (TextView) view;
        int value = Integer.parseInt(button.getText().toString());

        if (value < 0 || value > 9) return;

        presenter.handleNumberClick(value);
    }

    //endregion

    //region Contract

    @Override
    public void selectCell(int row, int column) {
        boardView.selectCell(row, column);

        middlePop.start();
    }

    @Override
    public void selectNumber(int value) {
        if (value < 1 || value > 9) {
            for (TextView button : numberEntryButtons) {
                button.setAlpha(constants.numberEntryDefaultAlpha);
            }
        } else {
            for (int i = 0; i < numberEntryButtons.length; ++i) {
                float alpha = (i + 1 == value)
                        ? constants.numberEntrySelectedAlpha
                        : constants.numberEntryDefaultAlpha;

                numberEntryButtons[i].setAlpha(alpha);
            }
        }

        lowPop.start();
    }

    @Override
    public void printScores(int playerScore1, int playerScore2) {
        // TODO: Set to strings file.
        playerScore1TextView.setText("Player Blue: " + playerScore1);
        playerScore2TextView.setText("Player Red: " + playerScore2);
    }

    @Override
    public void printBoard(int[][] board, String[][] cellOwners) {
        boardView.printBoard(board, cellOwners);
    }

    @Override
    public void alertBackToMenu() {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        // TODO: Set to strings file.
        alert.setTitle("LEAVE GAME");
        alert.setMessage("Are you sure you wish to leave this game? All progress will be lost.");
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "STAY",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                    }
                });

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "LEAVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        Context baseContext = getBaseContext();
                        Intent intent = new Intent(baseContext, MainActivity.class);

                        baseContext.startActivity(intent);
                    }
                });

        alert.show();
    }

    @Override
    public void alertEndOfGame(String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        // TODO: Set to strings file.
        alert.setTitle("GAME OVER");
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        openResultsActivity(null);
                    }
                });

        chronometer.stop();
        alert.show();
    }

    //endregion

    //region BoardGameView.Delegate

    @Override
    public void gameBoardViewDidClick(int row, int column) {
        presenter.handleCellClick(row, column);
    }

    //endregion

    //region Private

    private void setupSounds() {
        lowPop = MediaPlayer.create(this, R.raw.pop_low);
        middlePop = MediaPlayer.create(this, R.raw.pop_middle);
        highPop = MediaPlayer.create(this, R.raw.pop_high);
    }

    //endregion
}

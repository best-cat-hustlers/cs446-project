package com.bestCatHustlers.sukodublitz.game;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.ResultsActivity;

public class GameActivity extends AppCompatActivity implements GameContract.View, GameBoardView.Delegate {
    //region Properties

    private GameContract.Presenter presenter;
    private GameBoardView boardView;
    private TextView[] numberEntryButtons;

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
    }

    @Override
    public void printBoard(int[][] board, String[][] cellOwners) {
        boardView.printBoard(board, cellOwners);
    }

    @Override
    public void alertEndOfGame(String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();

        alert.setTitle("GAME OVER");
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        openResultsActivity(null);
                    }
                });

        alert.show();
    }

    //endregion

    //region BoardGameView.Delegate

    @Override
    public void gameBoardViewDidClick(int row, int column) {
        presenter.handleCellClick(row, column);
    }

    //endregion
}

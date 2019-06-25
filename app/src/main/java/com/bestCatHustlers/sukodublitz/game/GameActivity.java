package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.ResultsActivity;

public class GameActivity extends AppCompatActivity implements GameContract.View, GameBoardView.Delegate {
    private GameContract.Presenter presenter;
    private GameBoardView boardView;

    //region LifeCycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_game);


        boardView = (GameBoardView) findViewById(R.id.boardLayout);
        boardView.delegate = this;

        // TODO: Get model from intent extras.
        presenter = new GamePresenter(this, null);

        boardView.invalidate();
    }

    public void openResultsActivity(View view) {
        Intent intent = new Intent(this, ResultsActivity.class);

        startActivity(intent);
    }

    //endregion

    //region Contract


    @Override
    public GameBoardView.Cell getSelectedCell() {
        return boardView.getSelectedCell();
    }

    @Override
    public void selectCell(int row, int column) {
        boardView.setSelectedCell(row, column);
    }

    @Override
    public void deselectCells() {
        boardView.deselectCells();
    }

    @Override
    public void setScore(int score, String playerID) {
        Log.d("GAME_ACTIVITY", "setScore| score:" + score + " playerID:" + playerID);
    }

    //endregion

    //region BoardGameView.Delegate

    @Override
    public int valueForCell(int row, int column) {
        return presenter.getCellValueFor(row, column);
    }

    @Override
    public void gameBoardViewDidClick(int row, int column) {
        presenter.handleCellClick(row, column);
    }

    //endregion
}

package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.results.ResultsActivity;

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

        presenter.handleViewCreated();
    }

    public void openResultsActivity(View view) {
        Intent intent = new Intent(this, ResultsActivity.class);

        startActivity(intent);
    }

    //endregion

    //region Contract


    @Override
    public void selectCell(int row, int column) {
        boardView.selectCell(row, column);
    }

    @Override
    public void printBoard(int[][] board) {
        boardView.printBoard(board);
    }

    //endregion

    //region BoardGameView.Delegate

    @Override
    public void gameBoardViewDidClick(int row, int column) {
        presenter.handleCellClick(row, column);
    }

    //endregion
}

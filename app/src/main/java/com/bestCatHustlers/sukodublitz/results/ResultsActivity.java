package com.bestCatHustlers.sukodublitz.results;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bestCatHustlers.sukodublitz.setup.GameSetupActivity;
import com.bestCatHustlers.sukodublitz.main.MainActivity;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.game.GameBoardView;

public class ResultsActivity extends AppCompatActivity implements ResultsContract.View{
    private ResultsContract.Presenter presenter;
    private GameBoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        
        boardView = findViewById(R.id.result_boardLayout);

        presenter = new ResultsPresenter(this, getIntent().getExtras());
        presenter.handleViewCreated();
    }

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    public void openGameSetupActivity(View view) {
        Intent intent = new Intent(this, GameSetupActivity.class);

        startActivity(intent);
    }

    //region Contract


    @Override
    public void printTimeElapsed(String time) {
        TextView textTimeNumber = findViewById(R.id.textTimeNumber);

        textTimeNumber.setText(time);
    }

    @Override
    public void printBoard(int[][] board, String[][] cellOwners)
    {
        boardView.printBoard(board, cellOwners);
    }

    @Override
    public void printScores(int playerRedScore, int playerBlueScore)
    {
        TextView redScore = findViewById(R.id.textPlayerRedScore);
        TextView blueScore = findViewById(R.id.textPlayerBlueScore);

        redScore.setText(String.format("%d", playerRedScore));
        blueScore.setText(String.format("%d", playerBlueScore));
    }

    @Override
    public void printWinner(String id)
    {
        TextView winner = findViewById(R.id.textWinner);
        winner.setText(String.format("Player %s Wins!", id));
    }

    //endregion
}

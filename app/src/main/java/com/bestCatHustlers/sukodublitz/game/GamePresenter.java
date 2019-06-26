package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;
import android.widget.TextView;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.GameAI;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.R;

import java.util.logging.Handler;

public class GamePresenter implements GameContract.Presenter, GameAI.Delegate {
    //region Properties

    public static final String EXTRAS_KEY_BOARD_GAME = "BoardGame";
    public static final String EXTRAS_KEY_TIME_ELAPSED = "time_elapsed";

    private GameContract.View view;
    private BoardGame model;

    private GameAI ai;
    private Thread aiThread;

    private int selectedRow = -1;
    private int selectedColumn = -1;
    private int selectedNumber = 0;

    //endregion

    //region LifeCycle

    public GamePresenter(GameContract.View view, BoardGame model) {
        this.view = view;
        this.model = model;

        // TODO: Remove this test model once it can be passed in properly via intent.
        BoardGame testModel = new BoardGame();
        testModel.generateNewBoard();
        this.model = testModel;
    }

    //endregion

    //region Contract

    @Override
    public void handleViewCreated() {
        // TODO: Get player ID properly.
        Player player1 = model.getPlayer("1");
        Player player2 = model.getPlayer("2");

        view.printScores(player1.getScore(), player2.getScore());
        view.printBoard(model.getBoard(), model.getCellOwners());

        startAI(1000);
    }

    @Override
    public void handleCellClick(int row, int column) {
        if (model.getBoard()[row][column] > 0) return;

        if (row == selectedRow && column == selectedColumn) {
            selectedRow = -1;
            selectedColumn = -1;
        } else {
            selectedRow = row;
            selectedColumn = column;
        }

        if (shouldEnterSolution()) {
            enterSelectedSolution();

            selectedRow = -1;
            selectedColumn = -1;
        }

        view.selectCell(selectedRow, selectedColumn);
    }

    @Override
    public void handleNumberClick(int number) {
        if (selectedNumber == number) {
            selectedNumber = 0;
        } else {
            selectedNumber = number;
        }

        if (shouldEnterSolution()) {
            enterSelectedSolution();

            selectedNumber = 0;
            selectedRow = -1;
            selectedColumn = -1;

            view.selectCell(selectedRow, selectedColumn);
        }

        view.selectNumber(selectedNumber);
    }

    @Override
    public void handleOnBackPressed() {
        view.alertBackToMenu();
    }

    @Override
    public void prepareOpenResultsActivity(Intent intent) {
        // TODO: Add this to global constants.
        intent.putExtra(EXTRAS_KEY_BOARD_GAME, model);
        intent.putExtra(EXTRAS_KEY_TIME_ELAPSED, view.getTimeElapsed());
    }

    //endregion

    //region GameAI.Delegate

    @Override
    public void gameAiDidEnterSolution() {
        handleSolutionEntered();
    }


    //endregion

    //region Private

    private void startAI(int delay) {
        ai = new GameAI(model, delay, "2");
        ai.delegate = this;
        aiThread = new Thread(ai);

        aiThread.start();
    }

    private boolean shouldEnterSolution() {
        return (selectedNumber > 0 && selectedRow >= 0 && selectedColumn >= 0);
    }

    private void enterSelectedSolution() {
        if (!shouldEnterSolution()) return;

        // TODO: Get player ID properly.
        model.fillSquare(selectedRow, selectedColumn, selectedNumber, "1");

        handleSolutionEntered();
    }

    // TODO: Use model to determine if puzzle is solved properly.
    private boolean isPuzzleSolved() {
        int[][] board = model.getBoard();

        for (int row = 0; row < 9; ++row) {
            for (int column = 0; column < 9; ++column) {
                if (board[row][column] < 1) return false;
            }
        }

        return true;
    }

    private void handleSolutionEntered() {
        Player player1 = model.getPlayer("1");
        Player player2 = model.getPlayer("2");

        view.printScores(player1.getScore(), player2.getScore());
        view.printBoard(model.getBoard(), model.getCellOwners());

        if (isPuzzleSolved()) {
            // TODO: Create message strings.
            view.alertEndOfGame("Congratulations! You solved the puzzle. :)");
        }
    }

    //endregion
}

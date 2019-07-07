package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.GameAI;
import com.bestCatHustlers.sukodublitz.GameSetupActivity;
import com.bestCatHustlers.sukodublitz.Player;

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

    private long startTime = 0;
    private long endTime = 0;

    //endregion

    //region LifeCycle

    public GamePresenter(GameContract.View view, Bundle extras) {
        this.view = view;
        // TODO: Need to deal with these values
        if (extras != null) {
            boolean showPoints = extras.getBoolean(GameSetupActivity.EXTRAS_KEY_SHOW_POINTS);
            boolean showTimer = extras.getBoolean(GameSetupActivity.EXTRAS_KEY_SHOW_TIMER);
            boolean penaltyOn = extras.getBoolean(GameSetupActivity.EXTRAS_KEY_PENALTY_ON);
            int aiDifficulty = extras.getInt(GameSetupActivity.EXTRAS_KEY_AI_DIFFICULTY);
        }

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

        startTime = SystemClock.elapsedRealtime();

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
        int timeElapsed = (int) (endTime - startTime);

        intent.putExtra(EXTRAS_KEY_BOARD_GAME, model);
        intent.putExtra(EXTRAS_KEY_TIME_ELAPSED, timeElapsed);
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

        // If another player has entered a solution in the cell currently selected, force deselection.
        if (selectedRow >= 0 && selectedColumn >= 0 && model.getBoard()[selectedRow][selectedColumn] > 0) {
            selectedRow = -1;
            selectedColumn = -1;

            view.selectCell(selectedRow, selectedColumn);
        }

        if (isPuzzleSolved()) {
            endTime = SystemClock.elapsedRealtime();

            // TODO: Create message strings.
            view.alertEndOfGame("Congratulations! You solved the puzzle. :)");
        }
    }

    //endregion
}

package com.bestCatHustlers.sukodublitz.game;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.SystemClock;
import android.widget.Toast;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.GameAI;
import com.bestCatHustlers.sukodublitz.GameSetupActivity;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothService;
import com.bestCatHustlers.sukodublitz.utils.ParcelableByteUtil;

import static com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuPresenter.EXTRAS_KEY_IS_MULTI;

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

    private boolean isPointsShown = true;
    private boolean isTimerShown = true;
    private boolean isPenaltyOn= true;
    private int aiDifficulty = 5;

    private long startTime = 0;
    private long endTime = 0;

    private boolean isMultiplayerMode = false;

    private Constants constants;

    private class Constants {
        final int penaltyDelta = -10;
        final int aiBaseDelay = 5000;
    }

    //endregion

    //region LifeCycle

    public GamePresenter(GameContract.View view, Bundle extras) {
        this.view = view;

        constants = new Constants();

        // TODO: Remove this test model once it can be passed in properly via intent.
        BoardGame testModel = new BoardGame();
        testModel.addPlayer("1", Player.Team.BLUE);
        testModel.addPlayer("2", Player.Team.RED);
        testModel.generateNewBoard();

        model = testModel;

        configureGameWithSettings(extras);
    }

    //endregion

    //region Contract

    @Override
    public void handleViewCreated() {
        view.showPoints(isPointsShown);
        view.showTimer(isTimerShown);
        view.printScores(model.getTeamScore(Player.Team.BLUE), model.getTeamScore(Player.Team.RED));
        view.printBoard(model.getBoard(), model.getCellOwners());

        startTime = SystemClock.elapsedRealtime();

        if (aiDifficulty > 0) startAI();
    }

    @Override
    public void handleViewStarted() {
        if (isMultiplayerMode) {
            view.bindBluetoothService();
        }
    }

    @Override
    public void handleViewStopped() {
        if (aiThread != null) {
            aiThread.interrupt();
        }
    }

    @Override
    public void handleViewDestroyed() {
        if (aiThread != null) {
            aiThread.interrupt();
        }
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
        } else {
            view.playSound(R.raw.pop_low);
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
        } else {
            view.playSound(R.raw.pop_low);
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
        intent.putExtra(EXTRAS_KEY_BOARD_GAME, (Parcelable) model);
        intent.putExtra(EXTRAS_KEY_TIME_ELAPSED, timeElapsed);
    }

    //endregion

    //region GameAI.Delegate

    @Override
    public void gameAiDidEnterSolution() {
        view.playSound(R.raw.pop_high);
        handleSolutionEntered();
    }

    //endregion

    //region Private

    private void startAI() {
        ai = new GameAI(model, constants.aiBaseDelay / aiDifficulty, "2");
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

        view.playSound(R.raw.pop_middle);

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
        view.printScores(model.getTeamScore(Player.Team.BLUE), model.getTeamScore(Player.Team.RED));
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

    private void configureGameWithSettings(Bundle extras) {
        if (extras == null || model == null) return;

        isMultiplayerMode = extras.getBoolean(EXTRAS_KEY_IS_MULTI);

        isPointsShown = extras.getBoolean(GameSetupActivity.EXTRAS_KEY_SHOW_POINTS);
        isTimerShown = extras.getBoolean(GameSetupActivity.EXTRAS_KEY_SHOW_TIMER);
        isPenaltyOn = extras.getBoolean(GameSetupActivity.EXTRAS_KEY_PENALTY_ON);
        aiDifficulty = extras.getInt(GameSetupActivity.EXTRAS_KEY_AI_DIFFICULTY);

        model.setWrongAnsDelta(isPenaltyOn ? constants.penaltyDelta : 0);
    }

    //endregion
}

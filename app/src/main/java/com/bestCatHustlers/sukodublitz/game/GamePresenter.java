package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Message;
import android.util.Log;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.BoardGameSerializedObject;
import com.bestCatHustlers.sukodublitz.ExtrasKeys;
import com.bestCatHustlers.sukodublitz.GameAI;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothConstants;
import com.bestCatHustlers.sukodublitz.bluetooth.BluetoothMessage;
import com.bestCatHustlers.sukodublitz.settings.MainSettingsModel;
import com.bestCatHustlers.sukodublitz.utils.SerializableUtils;

import static com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuPresenter.EXTRAS_KEY_IS_MULTI;

public class GamePresenter implements GameContract.Presenter, GameAI.Delegate {
    //region Properties

    public static final String EXTRAS_KEY_BOARD_GAME = "boardGame";
    public static final String EXTRAS_KEY_TIME_ELAPSED = "timeElapsed";
    public static final String AI_ID = "2";

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

    private boolean isMultiplayer = false;
    private boolean isHost = false;

    private Constants constants;

    private class Constants {
        private class BluetoothTags {
            static final String solutionRequest = "solutionRequest";
            static final String updateBoardGame = "updateBoardGame";
        }

        static final int penaltyDelta = -10;
        static final int aiBaseDelay = 5000;
    }

    //endregion

    //region LifeCycle

    GamePresenter(GameContract.View view, Bundle extras) {
        this.view = view;

        configureGameWithSettings(extras);
    }

    //endregion

    //region Contract

    @Override
    public void handleViewCreated() {
        view.showPoints(isPointsShown);
        view.showTimer(isTimerShown);
        view.printScores(model.getTeamScore(Player.Team.BLUE), model.getTeamScore(Player.Team.RED));
        view.printBoard(model.getBoard(), model.getCellOwners(), model.getTeamPlayers(Player.Team.BLUE), model.getTeamPlayers(Player.Team.RED));

        startTime = SystemClock.elapsedRealtime();

        if (aiDifficulty > 0) startAI();
    }

    @Override
    public void handleViewStarted() {
        if (isMultiplayer) {
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

    @Override
    public void handleBluetoothMessageReceived(Message message) {
        switch (message.what) {
            case BluetoothConstants.MESSAGE_READ:
                handleBluetoothMessageRead(message);
                break;
            case BluetoothConstants.MESSAGE_WRITE:
                handleBluetoothMessageSent(message);
                break;
        }
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
        ai = new GameAI(model, Constants.aiBaseDelay / aiDifficulty, AI_ID);
        ai.delegate = this;
        aiThread = new Thread(ai);

        aiThread.start();
    }

    private boolean shouldEnterSolution() {
        return (selectedNumber > 0 && selectedRow >= 0 && selectedColumn >= 0);
    }

    private void enterSelectedSolution() {
        if (!shouldEnterSolution()) return;

        String playerID = MainSettingsModel.getInstance().getUserID();

        view.playSound(R.raw.pop_middle);

        if (isMultiplayer) {
            if (isHost) {
                model.fillSquare(selectedRow, selectedColumn, selectedNumber, playerID);
                handleSolutionEntered();

                propagateBoardGame();
            } else {
                SolutionRequest solution = new SolutionRequest(selectedRow, selectedColumn, selectedNumber, playerID);
                BluetoothMessage solutionRequestMessage = new BluetoothMessage(Constants.BluetoothTags.solutionRequest, solution);

                view.sendBluetoothMessage(solutionRequestMessage.serialized());
            }
        } else {
            model.fillSquare(selectedRow, selectedColumn, selectedNumber, playerID);
            handleSolutionEntered();
        }
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
        view.printBoard(model.getBoard(), model.getCellOwners(), model.getTeamPlayers(Player.Team.BLUE), model.getTeamPlayers(Player.Team.RED));

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
        if (extras == null) return;

        isHost = extras.getBoolean(ExtrasKeys.IS_HOST);
        isMultiplayer = extras.getBoolean(EXTRAS_KEY_IS_MULTI);
        isTimerShown = extras.getBoolean(ExtrasKeys.SHOULD_SHOW_TIMER);
        isPenaltyOn = extras.getBoolean(ExtrasKeys.SHOULD_USE_PENALTY);
        aiDifficulty = extras.getInt(ExtrasKeys.AI_DIFFICULTY);
        model = extras.getParcelable(ExtrasKeys.BOARD_GAME);

        if (model == null) {
            // TODO: Handle this fatal error.
            model = new BoardGame();
        }

        model.setWrongAnsDelta(isPenaltyOn ? Constants.penaltyDelta : 0);
    }

    private void handleBluetoothMessageRead(Message rawMessage) {
        byte[] buffer = (byte[]) rawMessage.obj;
        Object object = SerializableUtils.deserialize(buffer);

        if (object instanceof BluetoothMessage) {
            BluetoothMessage message = (BluetoothMessage) object;

            Log.d("GAME_BT_READ", "tag: " + message.tag + " payload: " + message.payload.getClass().getName());

            switch (message.tag) {
                case Constants.BluetoothTags.solutionRequest:
                    if (isHost) {
                        SolutionRequest solution = (SolutionRequest) message.payload;

                        model.fillSquare(solution);
                        view.playSound(R.raw.pop_high);

                        handleSolutionEntered();
                        propagateBoardGame();
                    }
                    break;
                case Constants.BluetoothTags.updateBoardGame:
                    if (!isHost) {
                        BoardGameSerializedObject serializedBoardGame = (BoardGameSerializedObject) message.payload;

                        model.syncWithSerializedObject(serializedBoardGame);
                        view.playSound(R.raw.pop_high);

                        handleSolutionEntered();
                    }
                    break;
            }
        }
    }

    private void handleBluetoothMessageSent(Message rawMessage) {
        byte[] buffer = (byte[]) rawMessage.obj;
        Object object= SerializableUtils.deserialize(buffer);

        if (object instanceof BluetoothMessage) {
            BluetoothMessage message = (BluetoothMessage) object;

            Log.d("GAME_BT_SENT", "tag: " + message.tag + " payload: " + message.payload.getClass().getName());
        }
    }

    private void propagateBoardGame() {
        if (!isHost) { return; }

        BoardGameSerializedObject serializedBoardGame = model.getSerializedObject();
        BluetoothMessage updateBoardGameMessage = new BluetoothMessage(Constants.BluetoothTags.updateBoardGame, serializedBoardGame);

        view.sendBluetoothMessage(updateBoardGameMessage.serialized());
    }

    //endregion
}

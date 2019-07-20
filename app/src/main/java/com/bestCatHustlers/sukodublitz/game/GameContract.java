package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;

public class GameContract {
    interface View {
        void bindBluetoothService();

        void selectCell(int row, int column);

        void selectNumber(int value);

        void printScores(int playerScore1, int playerScore2);

        void printBoard(int[][] board, String[][] cellOwners);

        void alertBackToMenu();

        void alertEndOfGame(String message);

        void playSound(int soundID);

        void showPoints(boolean shouldShowPoints);

        void showTimer(boolean shouldShowTimer);
    }

    interface Presenter {
        void handleViewCreated();

        void handleViewStarted();

        void handleViewDestroyed();

        void handleCellClick(int row, int column);

        void handleNumberClick(int number);

        void handleOnBackPressed();

        void prepareOpenResultsActivity(Intent intent);
    }
}

package com.bestCatHustlers.sukodublitz.game;

import android.content.Intent;

public class GameContract {
    interface View {
        void selectCell(int row, int column);

        void selectNumber(int value);

        void printScores(int playerScore1, int playerScore2);

        void printBoard(int[][] board, String[][] cellOwners);

        void alertBackToMenu();

        void alertEndOfGame(String message);
    }

    interface Presenter {
        void handleViewCreated();

        void handleCellClick(int row, int column);

        void handleNumberClick(int number);

        void handleOnBackPressed();

        void prepareOpenResultsActivity(Intent intent);
    }
}

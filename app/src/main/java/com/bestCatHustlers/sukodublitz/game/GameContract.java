package com.bestCatHustlers.sukodublitz.game;

public class GameContract {
    interface View {
        void selectCell(int row, int column);

        void selectNumber(int value);

        void printBoard(int[][] board, String[][] cellOwners);

        void alertEndOfGame(String message);
    }

    interface Presenter {
        void handleViewCreated();

        String getCellOwnerFor(int row, int column);

        void handleCellClick(int row, int column);

        void handleNumberClick(int number);
    }
}

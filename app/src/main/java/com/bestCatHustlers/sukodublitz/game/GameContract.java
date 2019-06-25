package com.bestCatHustlers.sukodublitz.game;

public class GameContract {
    interface View {
        void selectCell(int row, int column);

        void selectNumber(int value);

        void printBoard(int[][] board);
    }

    interface Presenter {
        void handleViewCreated();

        int getCellOwnerFor(int row, int column);

        void handleCellClick(int row, int column);

        void handleNumberClick(int number);
    }
}

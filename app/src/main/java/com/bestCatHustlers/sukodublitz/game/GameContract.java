package com.bestCatHustlers.sukodublitz.game;

public class GameContract {
    interface View {
        GameBoardView.Cell getSelectedCell();

        void selectCell(int row, int column);

        void deselectCells();

        void setScore(int score, String playerID);
    }

    interface Presenter {
        int getCellValueFor(int row, int column);

        int getCellOwnerFor(int row, int column);

        void handleCellClick(int row, int column);

        void handleNumberClick(int number);
    }
}

package com.bestCatHustlers.sukodublitz.game;

import com.bestCatHustlers.sukodublitz.BoardGame;

public class GamePresenter implements GameContract.Presenter {
    //region Properties

    private GameContract.View view;
    private BoardGame model;

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
        view.printBoard(model.getBoard());
    }

    @Override
    public int getCellOwnerFor(int row, int column) {
        return 0;
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

    //endregion

    //region Private

    private boolean shouldEnterSolution() {
        return (selectedNumber > 0 && selectedRow >= 0 && selectedColumn >= 0);
    }

    private void enterSelectedSolution() {
        if (!shouldEnterSolution()) return;

        // TODO: Get player ID properly.
        model.fillSquare(selectedRow, selectedColumn, selectedNumber, "1");
        view.printBoard(model.getBoard());
    }

    //endregion
}

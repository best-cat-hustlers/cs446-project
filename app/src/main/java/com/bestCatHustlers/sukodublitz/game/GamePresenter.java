package com.bestCatHustlers.sukodublitz.game;

import com.bestCatHustlers.sukodublitz.BoardGame;

public class GamePresenter implements GameContract.Presenter {
    //region Properties

    private GameContract.View view;
    private BoardGame model;

    //endregion

    //region LifeCycle

    public GamePresenter(GameContract.View view, BoardGame model) {
        this.view = view;
        this.model = model;
    }

    //endregion

    //region Contract

    @Override
    public int getCellValueFor(int row, int column) {
        return 0;
    }

    @Override
    public int getCellOwnerFor(int row, int column) {
        return 0;
    }

    @Override
    public void handleCellClick(int row, int column) {
        // TODO: Check if cell should be selected or not.
        view.selectCell(row, column);
    }

    @Override
    public void handleNumberClick(int number) {
        // TODO: Add view behaviour for selecting number entry interface.
    }

    //endregion
}

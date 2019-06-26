package com.bestCatHustlers.sukodublitz.results;

import com.bestCatHustlers.sukodublitz.BoardGame;

public class ResultsPresenter implements ResultsContract.Presenter
{
    private ResultsContract.View view;
    private BoardGame model;

    public ResultsPresenter(ResultsContract.View view, BoardGame model)
    {
        this.view = view;
        this.model = model;
    }

    @Override
    public void handleViewCreated()
    {
        // Tell the view to show the board and player scores
        view.printBoard(model.getBoard());
        view.printScores(model.getPlayer("1").getScore(), model.getPlayer("2").getScore());

        // TODO: Figure out how to tie players to colours
        String colour = model.getWinner().getId().equals("1") ? "Red" : "Blue";
        view.printWinner(colour);
    }
}

package com.bestCatHustlers.sukodublitz.results;

import android.os.Bundle;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.game.GamePresenter;

public class ResultsPresenter implements ResultsContract.Presenter
{
    private ResultsContract.View view;
    private BoardGame model;
    private int timeElapsed;

    public ResultsPresenter(ResultsContract.View view, Bundle extras)
    {
        this.view = view;
        this.model = extras.getParcelable(GamePresenter.EXTRAS_KEY_BOARD_GAME);
        this.timeElapsed = extras.getInt(GamePresenter.EXTRAS_KEY_TIME_ELAPSED);
    }

    @Override
    public void handleViewCreated()
    {
        // Tell the view to show the board and player scores
        view.printTimeElapsed(generateTimeElapsedString(timeElapsed));
        view.printBoard(model.getBoard(), model.getCellOwners());
        view.printScores(model.getPlayer("2").getScore(), model.getPlayer("1").getScore());

        // TODO: Figure out how to tie players to colours
        String colour = model.getWinner().getId().equals("2") ? "Red" : "Blue";
        view.printWinner(colour);
    }

    //region Private

    private String generateTimeElapsedString(int milliseconds) {
        int totalSeconds = (int) Math.floor(milliseconds / 1000);

        int seconds = totalSeconds % 60;
        int m = (totalSeconds - seconds) / 60;

        return String.format("%02d:%02d", m, seconds);
    }

    //endregion
}

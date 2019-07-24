package com.bestCatHustlers.sukodublitz.results;

import android.os.Bundle;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.game.GamePresenter;

import java.util.ArrayList;

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
        view.printBoard(model.getBoard(), model.getCellOwners(), model.getTeamPlayers(Player.Team.BLUE), model.getTeamPlayers(Player.Team.RED));
        view.printScores(model.getTeamScore(Player.Team.RED), model.getTeamScore(Player.Team.BLUE));
        String colour = model.getWinner() == Player.Team.RED ? "Red" : "Blue";
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

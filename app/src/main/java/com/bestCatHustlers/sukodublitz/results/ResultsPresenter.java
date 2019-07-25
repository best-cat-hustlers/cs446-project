package com.bestCatHustlers.sukodublitz.results;

import android.os.Bundle;

import com.bestCatHustlers.sukodublitz.BoardGame;
import com.bestCatHustlers.sukodublitz.Player;
import com.bestCatHustlers.sukodublitz.R;
import com.bestCatHustlers.sukodublitz.game.GamePresenter;

import java.util.ArrayList;

import static com.bestCatHustlers.sukodublitz.multiplayer.MultiplayerMenuPresenter.EXTRAS_KEY_IS_MULTI;

public class ResultsPresenter implements ResultsContract.Presenter
{
    private ResultsContract.View view;
    private BoardGame model;
    private int timeElapsed;
    private boolean isMultiplayerMode = false;

    public ResultsPresenter(ResultsContract.View view, Bundle extras)
    {
        this.view = view;
        if (extras != null) {
            this.model = extras.getParcelable(GamePresenter.EXTRAS_KEY_BOARD_GAME);
            this.timeElapsed = extras.getInt(GamePresenter.EXTRAS_KEY_TIME_ELAPSED);
            this.isMultiplayerMode = extras.getBoolean(EXTRAS_KEY_IS_MULTI);
        }
        if (isMultiplayerMode) {
            view.setMultiplayerScoreTitles();
        }
    }

    @Override
    public void handleViewCreated()
    {
        // Tell the view to show the board and player scores
        view.printTimeElapsed(generateTimeElapsedString(timeElapsed));
        view.printBoard(model.getBoard(), model.getCellOwners(), model.getTeamPlayers(Player.Team.BLUE), model.getTeamPlayers(Player.Team.RED));
        view.printScores(model.getTeamScore(Player.Team.RED), model.getTeamScore(Player.Team.BLUE));
        Player.Team winner = model.getWinner();
        if (winner == null) {
            view.printTie();
        } else {
            String side = winner == Player.Team.RED ? "Red" : "Blue";
            String title = isMultiplayerMode ? "Team" : "Player";
            int color = winner == Player.Team.RED ? R.color.secondaryColor : R.color.primaryColor;
            view.printWinner(title, side, color);
        }
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

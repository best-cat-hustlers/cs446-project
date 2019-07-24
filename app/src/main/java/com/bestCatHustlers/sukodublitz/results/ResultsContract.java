package com.bestCatHustlers.sukodublitz.results;

import com.bestCatHustlers.sukodublitz.Player;

import java.util.ArrayList;

public class ResultsContract
{
    interface View
    {
        void printTimeElapsed(String time);

        void printBoard(int[][] board, String[][] cellOwners, ArrayList<Player> bluePlayers, ArrayList<Player> redPlayers);

        void printScores(int playerRedScore, int playerBlueScore);

        void printWinner(String title, String id);

        void printTie();

        void setMultiplayerScoreTitles();
    }

    interface Presenter
    {
        void handleViewCreated();
    }
}

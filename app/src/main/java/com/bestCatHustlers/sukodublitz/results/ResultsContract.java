package com.bestCatHustlers.sukodublitz.results;

public class ResultsContract
{
    interface View
    {
        void printTimeElapsed(String time);

        void printBoard(int[][] board, String[][] cellOwners);

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

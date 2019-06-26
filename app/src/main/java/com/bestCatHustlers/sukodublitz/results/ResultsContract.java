package com.bestCatHustlers.sukodublitz.results;

public class ResultsContract
{
    interface View
    {
        void printBoard(int[][] board);
        void printScores(int playerRedScore, int playerBlueScore);
        void printWinner(String id);
    }

    interface Presenter
    {
        void handleViewCreated();
    }
}
package com.bestCatHustlers.sukodublitz;

import com.bestCatHustlers.sukodublitz.game.GamePresenter;

import java.util.Random;

public class FakeAIBlue implements Runnable
{
    public GameAI.Delegate delegate;

    private Random rand;
    private int[][] board;
    private BoardGame game;
    private String id;
    private PuzzleSolver solver;
    GamePresenter gamePresenter;

    public FakeAIBlue(BoardGame game, String id, GameAI.Delegate delegate)
    {
        this.game = game;
        this.id = id;
        this.solver = new PuzzleSolver();
        this.delegate = delegate;
        rand = new Random();
        gamePresenter = (GamePresenter) delegate;
    }

    @Override
    public void run()
    {
        int[][] solution = game.getSolution();
        try
        {
            Thread.sleep(1957); // Roughly 2.5 seconds
            //gamePresenter.handleCellClick(7,0);
            Thread.sleep(480);
            game.fillSquare(7, 0, 2, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(700); // Roughly 1.1 seconds
            //gamePresenter.handleCellClick(2,7);
            Thread.sleep(435);
            game.fillSquare(2,7, 6, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(250); // Roughly 0.5 seconds
            //gamePresenter.handleCellClick(8,1);
            Thread.sleep(300);
            game.fillSquare(8,1, 6, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(1499); // Roughly 2 seconds
            //gamePresenter.handleCellClick(1,5);
            Thread.sleep(499);
            game.fillSquare(1,5, 1, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(1100); // Roughly 1.5 seconds
            //gamePresenter.handleCellClick(6,4);
            Thread.sleep(523);
            game.fillSquare(6,4, 2, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(520); // Roughly 1 seconds
            //gamePresenter.handleCellClick(8,6);
            Thread.sleep(450);
            game.fillSquare(8,6, 2, id);
            delegate.gameAiDidEnterSolution();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

    }
}
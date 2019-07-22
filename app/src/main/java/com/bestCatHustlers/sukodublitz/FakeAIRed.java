package com.bestCatHustlers.sukodublitz;

import com.bestCatHustlers.sukodublitz.game.GamePresenter;

import java.util.Random;

public class FakeAIRed implements Runnable
{
    public GameAI.Delegate delegate;

    private Random rand;
    private int[][] board;
    private BoardGame game;
    private String id;
    private PuzzleSolver solver;
    GamePresenter gamePresenter;

    public FakeAIRed(BoardGame game, String id, GameAI.Delegate delegate)
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
            Thread.sleep(2202);
            gamePresenter.handleCellClick(1,3);
            Thread.sleep(530);
            game.fillSquare(1, 3, 5, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(650);
            gamePresenter.handleCellClick(4,4);
            Thread.sleep(380);
            game.fillSquare(4,4, 8, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(359);
            gamePresenter.handleCellClick(2,3);
            Thread.sleep(420);
            game.fillSquare(2,3, 8, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(1992);
            gamePresenter.handleCellClick(0,5);
            Thread.sleep(552);
            game.fillSquare(0,5, 9, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(1834);
            gamePresenter.handleCellClick(6,0);
            Thread.sleep(523);
            game.fillSquare(6,0, 5, id);
            delegate.gameAiDidEnterSolution();

            Thread.sleep(918);
            gamePresenter.handleCellClick(0,0);
            Thread.sleep(499);
            game.fillSquare(0,0, 4, id);
            delegate.gameAiDidEnterSolution();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

    }
}

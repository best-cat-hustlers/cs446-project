package com.bestCatHustlers.sukodublitz;

import java.util.Random;

public class FakeAIRed implements Runnable
{
    public GameAI.Delegate delegate;

    private Random rand;
    private int[][] board;
    private BoardGame game;
    private String id;
    private PuzzleSolver solver;

    public FakeAIRed(BoardGame game, String id, GameAI.Delegate delegate)
    {
        this.game = game;
        this.id = id;
        this.solver = new PuzzleSolver();
        this.delegate = delegate;
        rand = new Random();
    }

    @Override
    public void run()
    {
        int[][] solution = game.getSolution();
        try
        {
            Thread.sleep(2732);
            game.fillSquare(1, 3, 5, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(1135);
            game.fillSquare(4,4, 8, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(779);
            game.fillSquare(2,3, 8, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(2692);
            game.fillSquare(0,5, 9, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(2357);
            game.fillSquare(6,0, 5, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(1417);
            game.fillSquare(0,0, 4, id);
            delegate.gameAiDidEnterSolution();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

    }
}

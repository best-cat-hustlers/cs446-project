package com.bestCatHustlers.sukodublitz;

import java.util.Random;

public class FakeAIBlue implements Runnable
{
    public GameAI.Delegate delegate;

    private Random rand;
    private int[][] board;
    private BoardGame game;
    private String id;
    private PuzzleSolver solver;

    public FakeAIBlue(BoardGame game, String id, GameAI.Delegate delegate)
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
            Thread.sleep(2437); // Roughly 2.5 seconds
            game.fillSquare(7, 0, 2, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(1135); // Roughly 1.1 seconds
            game.fillSquare(2,7, 6, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(631); // Roughly 0.5 seconds
            game.fillSquare(1,5, 1, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(1998); // Roughly 2 seconds
            game.fillSquare(8,1, 6, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(1623); // Roughly 1.5 seconds
            game.fillSquare(6,4, 2, id);
            delegate.gameAiDidEnterSolution();
            Thread.sleep(956); // Roughly 1 seconds
            game.fillSquare(8,6, 2, id);
            delegate.gameAiDidEnterSolution();
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

    }
}
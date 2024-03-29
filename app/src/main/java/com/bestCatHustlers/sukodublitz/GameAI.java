package com.bestCatHustlers.sukodublitz;

import java.util.Random;

/*
AI class for single player Sudoku Blitz
AI should be run on it's own thread. Can be done so like the following:
Thread t = new Thread(new GameAI(boardGame, delay, id)).start()

AI thread should be interrupted instead of halting for integrity reasons.
Can be done so like the following:
t.interrupt()
 */
public class GameAI implements Runnable
{
    public Delegate delegate;

    private Random rand;
    private int[][] board;
    private int delayMs;
    private BoardGame game;
    private String id;
    private PuzzleSolver solver;
    private boolean paused = false;

    public interface Delegate {
        void gameAiDidEnterSolution();
    }

    public GameAI(BoardGame game, int delayMs, String id)
    {
        this.game = game;
        this.delayMs = delayMs;
        this.id = id;
        this.solver = new PuzzleSolver();
        rand = new Random();
    }

    // TODO: Obviously this is a very simply AI, figure out heuristics for more advanced AI
    @Override
    public void run()
    {
        int[][] solution = game.getSolution();
        while(true)
        {
            try
            {
                Thread.sleep(delayMs + (rand.nextInt(delayMs) * 2));
                while (paused) {
                    Thread.sleep(delayMs);
                }
                board = game.getBoard();
                solver.setPuzzle(board);
                ThreeTuple single = solver.findNakedSingle();
                // TODO: Test hidden single functionality
                if (single.valid == false)
                {
                    int row = -1;
                    int col = -1;
                    // Try to find empty square successful half the time
                    int upperBound = PuzzleGenerator.GRID_SIZE * (PuzzleGenerator.GRID_SIZE / 4);
                    for (int i = 0; i < upperBound; i++)
                    {
                        row = rand.nextInt(PuzzleGenerator.GRID_SIZE);
                        col = rand.nextInt(PuzzleGenerator.GRID_SIZE);
                        if (board[row][col] == 0) break; // Empty square found
                        row = -1;
                        col = -1;
                    }
                    if (row == -1 && col == -1) continue; // If no square can be found wait and try again
                    game.fillSquare(row, col, solution[row][col], id); // Just fill the square with
                }
                else
                {
                    game.fillSquare(single.row, single.col, single.val, id);
                }
                delegate.gameAiDidEnterSolution();
            }
            // Interrupting thread is better than stopping since it halts at a deterministic point
            catch(InterruptedException e)
            {
                // Ensure thread interrupt signal is propagated even if thread is nested
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    public void handlePause(){
        paused = true;
    }
    public void handleResume(){
        paused = false;
    }
}

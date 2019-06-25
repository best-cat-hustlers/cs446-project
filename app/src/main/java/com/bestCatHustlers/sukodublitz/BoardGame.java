package com.bestCatHustlers.sukodublitz;

import java.util.HashMap;
import java.util.concurrent.locks.*;
import java.util.Iterator;

public class BoardGame
{
    private HashMap<String, Player> players;
    private PuzzleGenerator puzzleGen;
    private Puzzle puzzle;
    private Lock lock;
    private int emptyCells;
    private final int SCORE_DELTA = 10; // TODO: Put this somewhere better

    BoardGame()
    {
        emptyCells = 0;
        lock = new ReentrantLock();
        players = new HashMap<String, Player>();
        // No idea if we want more than 2 players int the future so I'll add players
        // like this for now
        for (int i = 0; i < 2; i++)
        {
            String id = Integer.toString(i);
            players.put(id, new Player(id));
        }
        puzzleGen = new PuzzleGenerator();
        puzzle = puzzleGen.generatePuzzle();
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
        {
            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col++)
            {
                if (puzzle.puzzle[row][col] == 0) emptyCells++;
            }
        }
    }

    public void generateNewBoard()
    {
        try
        {
            lock.lock();
            puzzle = puzzleGen.generatePuzzle();
            for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
            {
                for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col++)
                {
                    if (puzzle.puzzle[row][col] == 0) emptyCells++;
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public Player getPlayer(String id)
    {
        return players.get(id);
    }

    public int[][] getBoard()
    {
        try
        {
            lock.lock();
            return puzzle.puzzle;
        }
        finally
        {
            lock.unlock();
        }
    }

    public int[][] getSolution()
    {
        return puzzle.solution;
    }

    public int getEmptyCells()
    {
        try
        {
            lock.lock();
            return emptyCells;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void fillSquare(int row, int col, int val, String id)
    {
        try
        {
            lock.lock();
            Player player = players.get(id);
            if (puzzle.puzzle[row][col] != 0 || player == null) return;
            if (puzzle.solution[row][col] != val)
            {
                player.modifyScore(-SCORE_DELTA); // TODO: Figure out scoring mechanism
                return;
            }
            puzzle.puzzle[row][col] = val;
            emptyCells--;
            player.modifyScore(SCORE_DELTA); // TODO: Figure out scoring mechanism
        }
        finally
        {
            lock.unlock();
        }
    }

    public Player getWinner()
    {
        if (getEmptyCells() != 0) return null;

        Player ret = null;
        Iterator it = players.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry next = (HashMap.Entry)it.next();
            Player player = (Player)next.getValue();
            if (ret == null)
            {
                ret = player;
            }
            // TODO: What if score is tied?
            else if (player.getScore() > ret.getScore())
            {
                ret = player;
            }
        }
        return ret;
    }
}

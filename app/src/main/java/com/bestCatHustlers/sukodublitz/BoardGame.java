package com.bestCatHustlers.sukodublitz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.concurrent.locks.*;
import java.util.Iterator;

// Class to model Sudoku Blitz game
// WARNING: When parceling BoardGame to another activity via intents, the programmer is responsible
// for ensuring that the lock isn't held by any thread. This is because the behaviour of Reentrant
// locks after deserialization is to set the lock as unlocked, regardless of of previous state.
public class BoardGame implements Parcelable
{
    private HashMap<String, Player> players;
    private PuzzleGenerator puzzleGen;
    private Puzzle puzzle;
    private ReentrantLock lock;
    private int emptyCells;
    private int correctAnsDelta;
    private int wrongAnsDelta;

    public BoardGame()
    {
        emptyCells = 0;
        correctAnsDelta = 10;
        wrongAnsDelta = -10;
        lock = new ReentrantLock();
        players = new HashMap<String, Player>();
        // No idea if we want more than 2 players int the future so I'll add players
        // like this for now
        for (int i = 1; i <= 2; i++)
        {
            String id = Integer.toString(i);
            players.put(id, new Player(id));
        }
        // This would be generateNewBoard() but Reentrant locks do not like to be called in the
        // constructor. This is a non-issue since nothing can manipulate the object until after
        // instantiation so there is no chance for a race condition
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

    public String[][] getCellOwners()
    {
        try
        {
            lock.lock();
            return puzzle.cellOwners;
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

    public void setCorrectAnsDelta(int delta) { correctAnsDelta = delta; }

    public void setWrongAnsDelta(int delta) { wrongAnsDelta = delta; }

    public void fillSquare(int row, int col, int val, String id)
    {
        try
        {
            lock.lock();
            Player player = players.get(id);
            if (puzzle.puzzle[row][col] != 0 || player == null) return;
            if (puzzle.solution[row][col] != val)
            {
                player.modifyScore(wrongAnsDelta);
                return;
            }
            puzzle.puzzle[row][col] = val;
            emptyCells--;
            player.modifyScore(correctAnsDelta);
            puzzle.cellOwners[row][col] = id;
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

    // Parcelable methods
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // Pack players hashmap
        int numPlayers = players.size();
        dest.writeInt(numPlayers);
        for(HashMap.Entry<String, Player> pair : players.entrySet())
        {
            dest.writeString(pair.getKey());
            dest.writeParcelable(pair.getValue(), 0);
        }

        dest.writeParcelable(puzzleGen, 0);
        dest.writeParcelable(puzzle, 0);
        dest.writeSerializable (lock); // Re-entrant lock is serializable
        dest.writeInt(emptyCells);
        dest.writeInt(correctAnsDelta);
        dest.writeInt(wrongAnsDelta);
    }

    // CREATOR field allows for generating instances of BoardGame from a Parcel
    public static final Parcelable.Creator<BoardGame> CREATOR = new Parcelable.Creator<BoardGame>()
    {
        @Override
        public BoardGame createFromParcel(Parcel in)
        {
            return new BoardGame(in);
        }

        @Override
        public BoardGame[] newArray(int size)
        {
            return new BoardGame[size];
        }
    };

    private BoardGame(Parcel in) {
        // Unpack players hashmap

        players = new HashMap<String, Player>();
        int numPlayers = in.readInt();
        for (int i = 0; i < numPlayers; i++) {
            String key = in.readString();
            Player player = in.readParcelable(Player.class.getClassLoader());
            players.put(key, player);
        }
        puzzleGen = in.readParcelable(PuzzleGenerator.class.getClassLoader());
        puzzle = in.readParcelable(Puzzle.class.getClassLoader());
        lock = (ReentrantLock) in.readSerializable();
        emptyCells = in.readInt();
        correctAnsDelta = in.readInt();
        wrongAnsDelta = in.readInt();
    }
}

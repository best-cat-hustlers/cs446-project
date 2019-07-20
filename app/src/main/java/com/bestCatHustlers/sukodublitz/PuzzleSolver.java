package com.bestCatHustlers.sukodublitz;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PuzzleSolver
{
    private int[][] candidates;
    private int[][] puzzle;

    private final int allAllowed = 0b111111111;
    public PuzzleSolver()
    {
        candidates = new int[PuzzleGenerator.GRID_SIZE][PuzzleGenerator.GRID_SIZE];
        puzzle = new int[PuzzleGenerator.GRID_SIZE][];
    }

    public void setPuzzle(int[][] puzzle)
    {
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
        {
            this.puzzle[row] = puzzle[row].clone();
        }
        findCandidates();
    }

    public ThreeTuple findNakedSingle()
    {
        ArrayList<ThreeTuple> singles = new ArrayList<ThreeTuple>();
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
        {
            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col++)
            {
                if (isOnlyOneBitSet(candidates[row][col]))
                {
                    ThreeTuple tuple = new ThreeTuple();
                    tuple.valid = true;
                    tuple.row = row;
                    tuple.col = col;
                    tuple.val = getLowestOrderBit(candidates[row][col]);
                    singles.add(tuple);
                }
            }
        }
        if (singles.size() == 0) return new ThreeTuple();
        // Return a random single that was found
        Random rand = new Random();
        return singles.get(rand.nextInt(singles.size()));
    }

    public ThreeTuple findHiddenSingle()
    {
        ThreeTuple ret = findHiddenSingleByRow();
        if (ret.valid) return ret;
        ret = findHiddenSingleByCol();
        if (ret.valid) return ret;
        ret = findHiddenSingleByGrid();
        return ret;
    }

    private ThreeTuple findHiddenSingleByGrid()
    {
        ArrayList<ThreeTuple> singles = new ArrayList<ThreeTuple>();

        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row += 3)
        {
            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col += 3)
            {
                // Initialize hashmap
                HashMap< Integer, ArrayList< Pair<Integer, Integer> > > possibleCells = new HashMap<>();
                for (int i = 1; i <= PuzzleGenerator.GRID_SIZE; i++)
                {
                    possibleCells.put(i, new ArrayList< Pair<Integer, Integer> >());
                }

                for (int curRow = row; curRow < row + 3; curRow++)
                {
                    for (int curCol = col; curCol < col + 3; curCol++)
                    {
                        int currentCellCandidates = candidates[curRow][curCol];
                        if (currentCellCandidates == 0) continue;
                        while(currentCellCandidates != 0)
                        {
                            int pos = getLowestOrderBit(currentCellCandidates);
                            currentCellCandidates = currentCellCandidates >> 2;
                            possibleCells.get(pos).add(new Pair(curRow, curCol));
                        }
                    }
                }

                // Check if there is a value that can only be placed in one cell
                for (int i = 1; i <= PuzzleGenerator.GRID_SIZE; i++)
                {
                    if (possibleCells.get(i).size() == 1)
                    {
                        Pair<Integer, Integer> pair = possibleCells.get(i).get(0);
                        ThreeTuple tuple = new ThreeTuple();
                        tuple.valid = true;
                        tuple.row = pair.first;
                        tuple.col = pair.second;
                        tuple.val = i;
                        singles.add(tuple);
                    }
                }
            }
        }

        if (singles.size() == 0) return new ThreeTuple();
        // Return a random single that was found
        Random rand = new Random();
        return singles.get(rand.nextInt(singles.size()));
    }

    private ThreeTuple findHiddenSingleByCol()
    {
        transpose(candidates);
        ThreeTuple ret = findHiddenSingleByRow();
        int temp = ret.row;
        ret.row = ret.col;
        ret.col = temp;
        transpose(candidates);
        return ret;
    }

    private ThreeTuple findHiddenSingleByRow()
    {
        ArrayList<ThreeTuple> singles = new ArrayList<ThreeTuple>();
        // Find hidden singles per row
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
        {
            // Initialize hashmap
            HashMap< Integer, ArrayList< Pair<Integer, Integer> > > possibleCells = new HashMap<>();
            for (int i = 1; i <= PuzzleGenerator.GRID_SIZE; i++)
            {
                possibleCells.put(i, new ArrayList< Pair<Integer, Integer> >());
            }

            // Update hashmap with all cells that can possibly hold value
            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col++)
            {
                int currentCellCandidates = candidates[row][col];
                if (currentCellCandidates == 0) continue;
                while(currentCellCandidates != 0)
                {
                    int pos = getLowestOrderBit(currentCellCandidates);
                    currentCellCandidates = currentCellCandidates >> 2;
                    possibleCells.get(pos).add(new Pair(row, col));
                }
            }

            // Check if there is a value that can only be placed in one cell
            for (int i = 1; i <= PuzzleGenerator.GRID_SIZE; i++)
            {
                if (possibleCells.get(i).size() == 1)
                {
                    Pair<Integer, Integer> pair = possibleCells.get(i).get(0);
                    ThreeTuple tuple = new ThreeTuple();
                    tuple.valid = true;
                    tuple.row = pair.first;
                    tuple.col = pair.second;
                    tuple.val = i;
                    singles.add(tuple);
                }
            }
        }

        if (singles.size() == 0) return new ThreeTuple();
        // Return a random single that was found
        Random rand = new Random();
        return singles.get(rand.nextInt(singles.size()));
    }

    // The state of the puzzle can change without the AI doing anything so unfortunately we must
    // generate cell candidates every time
    private void findCandidates()
    {
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
        {
            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col++)
            {
                if (puzzle[row][col] != 0)
                {
                    candidates[row][col] = 0;
                    continue;
                }
                candidates[row][col] = allAllowed; // Initial configuration of candidates
            }
        }
        // Find candidates for each row
        findCandidatesByRow();
        // Transpose to make columns into rows
        transpose(puzzle);
        transpose(candidates);
        // Find candidates for each column
        findCandidatesByRow();
        // Restore original configuration of puzzle
        transpose(puzzle);
        transpose(candidates);

        // Find candidates by grid
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row += 3)
        {
            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col += 3)
            {
                findCandidatesByGrid(row, col);
            }
        }
    }

    private void findCandidatesByRow()
    {
        // Find candidates for each row
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
        {
            int[] puzzleRow = puzzle[row];
            int disallowedCandidates = 0b0;
            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col++)
            {
                if (puzzleRow[col] == 0) continue;
                disallowedCandidates |= 1 << (puzzleRow[col] - 1);
            }

            for (int col = 0; col < PuzzleGenerator.GRID_SIZE; col++)
            {
                removeCandidatesFromCell(row, col, disallowedCandidates);
            }
        }
    }

    // Find candidates by the 3x3 grid marked by the top left hand corner at (row, col)
    private void findCandidatesByGrid(int row, int col)
    {
        int disallowedCandidates = 0b0;
        for (int curRow = row; curRow < row + 3; curRow++)
        {
            for (int curCol = col; curCol < col + 3; curCol++)
            {
                if (puzzle[curRow][curCol] == 0) continue;
                disallowedCandidates |= 1 << (puzzle[curRow][curCol] - 1);
            }
        }
        for (int curRow = row; curRow < row + 3; curRow++)
        {
            for (int curCol = col; curCol < col + 3; curCol++)
            {
                removeCandidatesFromCell(curRow, curCol, disallowedCandidates);
            }
        }
    }

    // Removes candidates from a cell
    private void removeCandidatesFromCell(int row, int col, int candidate)
    {
        candidates[row][col] &= ~candidate;
    }

    // Transpose a matrix
    private void transpose(int[][] arr)
    {
        for (int row = 0; row < PuzzleGenerator.GRID_SIZE; row++)
        {
            for (int i = row; i < PuzzleGenerator.GRID_SIZE; i++)
            {
                int temp = arr[row][i];
                arr[row][i] = arr[i][row];
                arr[i][row] = temp;
            }
        }
    }

    // Checks if there is only one bit set in the integer using power of 2 checking algorithm
    private boolean isOnlyOneBitSet(int val)
    {
        // Apparently ints don't implicitly cast to booleans so check for non-zero
        return (val != 0) && ((val & (val - 1)) == 0);
    }

    // Assumes val is not zero
    private int getLowestOrderBit(int val)
    {
        int ret = 1;
        while (val != 0)
        {
            if ((val & 1) == 1) break;
            val = val >> 1;
            ret += 1;
        }
        return ret;
    }
}

// Apparently Java doesn't have tuples so need a user defined class for one
class ThreeTuple
{
    public ThreeTuple() { valid = false; row = 0; col = 0; val = 0; }
    public boolean valid; // Flag for determining if ThreeTuple is valid
    public int row;
    public int col;
    public int val;
}
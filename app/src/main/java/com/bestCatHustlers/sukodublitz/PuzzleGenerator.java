package com.bestCatHustlers.sukodublitz;

// TODO: delete this import
import android.util.Log;

import java.util.Collections;
import java.util.Arrays;

// A class to generate sudoku puzzles and their solutions
public class PuzzleGenerator
{
    private int[][] seeds;
    public static final int GRID_SIZE = 9;

    PuzzleGenerator()
    {
        // TODO: Create and store multiple seeds
        seeds = new int[][]{{0, 0, 0, 2, 6, 0, 7, 0, 1},
                                {6, 8, 0, 0, 7, 0, 0, 9, 0},
                                {1, 9, 0, 0, 0, 4, 5, 0, 0},
                                {8, 2, 0, 1, 0, 0, 0, 4, 0},
                                {0, 0, 4, 6, 0, 2, 9, 0, 0},
                                {0, 5, 0, 0, 0, 3, 0, 2, 8},
                                {0, 0, 9, 3, 0, 0, 0, 7, 4},
                                {0, 4, 0, 0, 5, 0, 0, 3, 6},
                                {7, 0, 3, 0, 1, 8, 0, 0, 0}};
    }

    public Puzzle generatePuzzle()
    {
        // TODO: Randomly pick a seed

        // Permute the seed to generate a new board
        // 3,359,232 different boards per seed are possible through this, assuming seeds are not
        // the equivalent to each other
        shuffle(seeds);
        rotate(seeds);
        shuffle(seeds);
        // TODO: Permute band, stack, and cells, randomize rotations
        for (int i = 0; i < GRID_SIZE; i++)
        {
            Log.d("TEST", Arrays.toString(seeds[i]));
        }
        Log.d("TEST", "------------------------------------------------");

        int[][] solution = new int[GRID_SIZE][];
        for (int i = 0; i < GRID_SIZE; i++)
        {
            solution[i] = seeds[i].clone();
        }
        backtrack(solution);

        for (int i = 0; i < GRID_SIZE; i++)
        {
            Log.d("TEST", Arrays.toString(solution[i]));
        }
        return new Puzzle(seeds, solution);
    }

    // Rotate matrix 90 degrees clockwise
    private void rotate(int[][] arr)
    {
        // Transpose
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int i = row; i < GRID_SIZE; i++)
            {
                int temp = arr[row][i];
                arr[row][i] = arr[i][row];
                arr[i][row] = temp;
            }
        }

        // Reverse rows
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int i = 0; i < GRID_SIZE /2; i++)
            {
                int temp = arr[row][i];
                int opposite = GRID_SIZE - 1 - i;
                arr[row][i] = arr[row][opposite];
                arr[row][opposite] = temp;
            }
        }
    }

    private void shuffle(int[][] arr)
    {
        // TODO: Refactor to make less ugly and inefficient
        int[][] temp = new int[3][];
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                temp[j] = arr[i * 3 + j].clone();
            }
            Collections.shuffle(Arrays.asList(temp));
            for (int j = 0; j < 3; j++)
            {
                arr[i * 3 + j] = temp[j].clone();
            }
        }
    }

    // "Simple" backtracking sudoku solver with some additional rules.
    // Generates solution for sudoku puzzle. Assumes there is a solution
    boolean backtrack(int[][] arr)
    {
        // TODO: Refactor to make faster
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int col = 0; col < GRID_SIZE; col++)
            {
                if (arr[row][col] == 0)
                {
                    for (int n = 1; n <= GRID_SIZE; n++)
                    {
                        if (allowed(arr, row, col, n))
                        {
                            arr[row][col] = n;
                            if (backtrack(arr))
                            {
                                return true;
                            }
                            else
                            {
                                arr[row][col] = 0;
                            }
                        }
                    }
                    // No value is allowed in the square
                    if (arr[row][col] == 0) return false;
                }
            }
        }
        return true;
    }

    // Checks to see if val is contained in the corresponding sudoku grid at position
    // (row, col).
    private boolean allowed(int[][] arr, int row, int col, int val)
    {
        // Check grid
        // Find top left corner
        int cornerCol = (col / 3) * 3;
        int cornerRow = (row / 3) * 3;


        for (int curRow = cornerRow; curRow < cornerRow + 3; curRow++)
        {
            for (int curCol = cornerCol; curCol < cornerCol + 3; curCol++)
            {
                if (arr[curRow][curCol] == val) return false;
            }
        }
        // Check row
        for (int i = 0; i < GRID_SIZE; i++)
        {
            if (arr[row][i] == val) return false;
        }

        // Check col
        for (int i = 0; i < GRID_SIZE; i++)
        {
            if (arr[i][col] == val) return false;
        }
        return true;
    }
}

// Struct for pairing puzzles with solutions
// Elements of puzzle and solution can be accessed with (row, col) indexing
class Puzzle
{
    Puzzle(int[][] puzzle, int[][] solution)
    {
        this.puzzle = puzzle;
        this.solution = solution;
    }
    public int[][] puzzle;
    public int[][] solution;
}
package com.bestCatHustlers.sukodublitz;

import java.io.Serializable;
import java.util.HashMap;

public class BoardGameSerializedObject implements Serializable
{
    public HashMap<String, Player> players;
    Puzzle puzzle;
    public BoardGameSerializedObject(HashMap<String, Player> players, Puzzle puzzle)
    {
        this.players = players;
        this.puzzle = puzzle;
    }
}
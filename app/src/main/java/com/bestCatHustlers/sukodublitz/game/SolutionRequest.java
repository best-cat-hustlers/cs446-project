package com.bestCatHustlers.sukodublitz.game;

import java.io.Serializable;

public class SolutionRequest implements Serializable {
    public int row;
    public int column;
    public int number;
    public String playerID;

    public SolutionRequest(int row, int column, int number, String playerID) {
        this.row = row;
        this.column = column;
        this.number = number;
        this.playerID = playerID;
    }
}

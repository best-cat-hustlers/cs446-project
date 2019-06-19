package com.bestCatHustlers.sukodublitz;

public class Player
{
    private String id;
    private int score;

    Player(String id)
    {
        this.id = id;
        this.score = 0;
    }

    public String getId()
    {
        return this.id;
    }

    public int getScore()
    {
        return this.score;
    }

    // Modifies player's score by delta points
    public void modifyScore(int delta)
    {
        this.id += delta;
    }
}

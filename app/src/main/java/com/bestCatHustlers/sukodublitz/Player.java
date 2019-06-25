package com.bestCatHustlers.sukodublitz;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable
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
        this.score += delta;
    }

    // Parcelable methods
    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeInt(score);
    }

    // CREATOR field allows for generating instances of Player from a Parcel
    public static final Parcelable.Creator<Player> CREATOR
            = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    private Player(Parcel in) {
        id = in.readString();
        score = in.readInt();
    }
}
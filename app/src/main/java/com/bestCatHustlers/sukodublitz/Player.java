package com.bestCatHustlers.sukodublitz;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Player implements Parcelable, Serializable
{
    public enum Team
    {
        RED(1), BLUE(2);

        private int value;

        Team (int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private String id;
    private Team team;
    private int score;

    public Player(String id, Team team)
    {
        this.id = id;
        this.team = team;
        this.score = 0;
    }

    public String getId()
    {
        return this.id;
    }

    public Team getTeam() { return this.team; }

    public void setTeam(Team team) { this.team = team; }

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
        dest.writeString(team.name());
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
        team = Team.valueOf(in.readString());
        score = in.readInt();
    }
}

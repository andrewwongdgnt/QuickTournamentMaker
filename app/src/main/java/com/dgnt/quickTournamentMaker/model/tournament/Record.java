package com.dgnt.quickTournamentMaker.model.tournament;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Owner on 3/13/2016.
 */
public class Record implements Parcelable {

    //need empty constructor because Record implements Parcelable
    public Record(){
    }

    private int wins;
    private int losses;
    private int ties;

    public int getWins() {
        return wins;
    }

    public void adjustWinsBy(final int adjuster){
        this.wins+=adjuster;
    }

    public int getLosses() {
        return losses;
    }

    public void adjustLossesBy(final int adjuster){
        this.losses+=adjuster;
    }

    public int getTies() {
        return ties;
    }

    public void adjustTiesBy(final int adjuster){
        this.ties+=adjuster;
    }

    public String toString(){
        return "("+getWins()+"-"+getLosses()+"-"+getTies()+")";
    }

    protected Record(Parcel in) {
        wins = in.readInt();
        losses = in.readInt();
        ties = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(wins);
        dest.writeInt(losses);
        dest.writeInt(ties);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}
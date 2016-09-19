package com.dgnt.quickTournamentMaker.model.history;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Owner on 9/15/2016.
 */
public class HistoricalRound implements Parcelable {

    private int roundGroupIndex;
    private int roundIndex;
    private String title;
    private String note;
    private int color;

    public HistoricalRound(final int roundGroupIndex, final int roundIndex, final String title, final String note, final int color) {
        this.roundGroupIndex = roundGroupIndex;
        this.roundIndex = roundIndex;
        this.title = title;
        this.note = note;
        this.color = color;
    }

    public int getRoundGroupIndex() {
        return roundGroupIndex;
    }

    public int getRoundIndex() {
        return roundIndex;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public int getColor() {
        return color;
    }

    protected HistoricalRound(Parcel in) {
        roundGroupIndex = in.readInt();
        roundIndex = in.readInt();
        title = in.readString();
        note = in.readString();
        color = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roundGroupIndex);
        dest.writeInt(roundIndex);
        dest.writeString(title);
        dest.writeString(note);
        dest.writeInt(color);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HistoricalRound> CREATOR = new Parcelable.Creator<HistoricalRound>() {
        @Override
        public HistoricalRound createFromParcel(Parcel in) {
            return new HistoricalRound(in);
        }

        @Override
        public HistoricalRound[] newArray(int size) {
            return new HistoricalRound[size];
        }
    };
}
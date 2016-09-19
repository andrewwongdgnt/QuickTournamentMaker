package com.dgnt.quickTournamentMaker.model.history;

import android.os.Parcel;
import android.os.Parcelable;

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;

/**
 * Created by Owner on 7/6/2016.
 */
public class HistoricalMatchUp implements Parcelable {
    private int roundGroupIndex;
    private int roundIndex;
    private int matchUpIndex;
    private String note;
    private int color;
    private MatchUp.MatchUpStatus matchUpStatus;

    public HistoricalMatchUp(final int roundGroupIndex, final int roundIndex, final int matchUpIndex, final String note, final int color, final MatchUp.MatchUpStatus matchUpStatus) {
        this.roundGroupIndex = roundGroupIndex;
        this.roundIndex = roundIndex;
        this.matchUpIndex = matchUpIndex;
        this.note = note;
        this.color = color;
        this.matchUpStatus = matchUpStatus;
    }

    public int getRoundGroupIndex() {
        return roundGroupIndex;
    }

    public int getRoundIndex() {
        return roundIndex;
    }

    public int getMatchUpIndex() {
        return matchUpIndex;
    }

    public int getColor() {
        return color;
    }

    public String getNote() {
        return note;
    }

    public MatchUp.MatchUpStatus getMatchUpStatus() {
        return matchUpStatus;
    }

    protected HistoricalMatchUp(Parcel in) {
        roundGroupIndex = in.readInt();
        roundIndex = in.readInt();
        matchUpIndex = in.readInt();
        note = in.readString();
        color = in.readInt();
        matchUpStatus = MatchUp.MatchUpStatus.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roundGroupIndex);
        dest.writeInt(roundIndex);
        dest.writeInt(matchUpIndex);
        dest.writeString(note);
        dest.writeInt(color);
        dest.writeString(matchUpStatus.name());
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HistoricalMatchUp> CREATOR = new Parcelable.Creator<HistoricalMatchUp>() {
        @Override
        public HistoricalMatchUp createFromParcel(Parcel in) {
            return new HistoricalMatchUp(in);
        }

        @Override
        public HistoricalMatchUp[] newArray(int size) {
            return new HistoricalMatchUp[size];
        }
    };
}
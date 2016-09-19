package com.dgnt.quickTournamentMaker.model.tournament;

import android.os.Parcel;
import android.os.Parcelable;

import com.dgnt.quickTournamentMaker.eventListener.OnParticipantUpdateListener;
import com.dgnt.quickTournamentMaker.model.IKeyable;
import com.dgnt.quickTournamentMaker.model.management.Person;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;

/**
 * Created by Owner on 3/13/2016.
 */
public class Participant implements IKeyable, Parcelable, Comparable<Participant> {

    final public static Participant NULL_PARTICIPANT = new Participant(new Person("", ""), ParticipantType.NULL);

    final public static Participant BYE_PARTICIPANT = new Participant(new Person("", ""), ParticipantType.BYE);

    public enum ParticipantType {
        NORMAL, NULL, BYE
    }

    private Person person;
    private int color;
    private String note;
    private String displayName;
    private Record record;
    private ParticipantType participantType;

    public Participant(final Person person, final ParticipantType participantType) {
        this.person = person;
        this.displayName = person.getName();
        this.record = new Record();
        this.color = TournamentUtil.DEFAULT_DISPLAY_COLOR;
        this.note = person.getNote();
        this.participantType = participantType;
    }

    public Participant(final Person person) {
        this(person, ParticipantType.NORMAL);
    }

    private OnParticipantUpdateListener onParticipantUpdateListener;

    public void setOnParticipantUpdateListener(OnParticipantUpdateListener onParticipantUpdateListener) {
        this.onParticipantUpdateListener = onParticipantUpdateListener;
    }

    public void dispatchParticipantUpdateEvent() {
        if (onParticipantUpdateListener != null) {
            onParticipantUpdateListener.onParticipantUpdate(this);
        }
    }

    public String getName() {
        return person.getName();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        if (!isBye() && !isNull()) {
            this.displayName = displayName;
            dispatchParticipantUpdateEvent();
        }
    }

    public int getColor() {
        return color == 0 ? TournamentUtil.DEFAULT_DISPLAY_COLOR : color;
    }

    public void setColor(int color) {
        if (!isBye() && !isNull()) {
            this.color = color;
            dispatchParticipantUpdateEvent();
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        if (!isBye() && !isNull()) {
            this.note = note;
            dispatchParticipantUpdateEvent();
        }
    }

    public String getKey() {
        return getName()+participantType.name();
    }

    public int getWins() {
        return record.getWins();
    }

    public void adjustWinsBy(final int adjuster) {
        record.adjustWinsBy(adjuster);
        dispatchParticipantUpdateEvent();
    }

    public int getLosses() {
        return record.getLosses();
    }

    public void adjustLossesBy(final int adjuster) {
        record.adjustLossesBy(adjuster);
        dispatchParticipantUpdateEvent();
    }

    public int getTies() {
        return record.getTies();
    }

    public void adjustTiesBy(final int adjuster) {
        record.adjustTiesBy(adjuster);
        dispatchParticipantUpdateEvent();
    }

    public String getRecordAsDisplay() {
        return record.toString();
    }

    public ParticipantType getParticipantType() {
        return participantType;
    }

    public boolean isBye() {
        return getParticipantType() == ParticipantType.BYE;
    }

    public boolean isNull() {
        return getParticipantType() == ParticipantType.NULL;
    }


    public boolean isNormal() {
        return !isNull() && !isBye();
    }

    @Override
    public int compareTo(Participant otherParticipant) {
        return getDisplayName().compareTo(otherParticipant.getDisplayName());
    }

    @Override
    public String toString() {
        return getName() + " " + record;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Participant)) return false;
        Participant otherOne = (Participant) other;

        if (getKey().equals(otherOne.getKey())) return true;

        return false;

    }

    protected Participant(Parcel in) {
        person = (Person) in.readValue(Person.class.getClassLoader());
        color = in.readInt();
        displayName = in.readString();
        note = in.readString();
        record = (Record) in.readValue(Record.class.getClassLoader());
        participantType = (ParticipantType) in.readValue(ParticipantType.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(person);
        dest.writeInt(color);
        dest.writeString(displayName);
        dest.writeString(note);
        dest.writeValue(record);
        dest.writeValue(participantType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };
}
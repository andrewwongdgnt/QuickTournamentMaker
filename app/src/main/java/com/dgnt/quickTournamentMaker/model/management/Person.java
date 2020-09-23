package com.dgnt.quickTournamentMaker.model.management;

import android.os.Parcel;
import android.os.Parcelable;

import com.dgnt.quickTournamentMaker.model.IKeyable;

/**
 * Created by Owner on 3/13/2016.
 */
public class Person implements IKeyable, Comparable<Person>, Parcelable {

    private String name;
    private String note;

    public Person(final String name, final String note) {
        this.name = name;
        this.note = note;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getKey() {
        return getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Person)) {
            return false;
        }
        Person otherOne = (Person) other;

        if (getKey().equals(otherOne.getKey())) {
            return true;
        }

        return false;

    }

    @Override
    public int compareTo(Person otherPerson) {
        return getName().compareTo(otherPerson.getName());
    }

    protected Person(Parcel in) {
        name = in.readString();
        note = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(note);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
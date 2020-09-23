package com.dgnt.quickTournamentMaker.model.management;

import com.dgnt.quickTournamentMaker.model.IKeyable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 5/28/2016.
 */
public class Group implements IKeyable, Comparable<Group> {

    private List<Person> personList;
    private String name;
    private String note;
    private boolean favourite;


    public Group(final String name, final String note, final boolean favourite){
        this.name = name;
        this.note = note;
        this.favourite = favourite;
        this.personList = new ArrayList<>();
    }

    public Group(final String name, final String note, final boolean favourite, final List<Person> personList){
        this(name,note,favourite);
        this.personList = personList!=null ? personList : new ArrayList<Person>();
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public int getTotalPersons(){
        return personList.size();
    }

    public Person getPersonAt(final int index){
        return personList.get(index);
    }

    public void addPersonAt(final int index, final Person person){
        personList.add(index, person);
    }

    public void addPerson(final Person person){
        personList.add(person);
    }

    public Person removePersonAt(final int index){
        return personList.remove(index);
    }

    public boolean removePerson(final Person person){
        return personList.remove(person);
    }

    public String getKey(){
        return getName();
    }

    public int compareTo(Group otherGroup) {

        if (isFavourite() && !otherGroup.isFavourite()) {
            return -1;
        }

        if (!isFavourite() && otherGroup.isFavourite()) {
            return 1;
        }

        return getName().compareTo(otherGroup.getName());
    }
}

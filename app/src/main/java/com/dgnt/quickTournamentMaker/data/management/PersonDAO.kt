package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dgnt.quickTournamentMaker.model.management.Person

class PersonDAO {
    // FIXME: A fake database table
    private val personList = mutableListOf<Person>()


    private val persons = MutableLiveData<List<Person>>()

    init {
        persons.value = personList
    }

    fun addPerson(person: Person) {
        personList.add(person)
        persons.value = personList
    }

    fun deletePerson(person: Person){
        personList.removeAll {  p-> p.equals(person.key)}
    }

    fun getPersons() = persons as LiveData<List<Person>>
}
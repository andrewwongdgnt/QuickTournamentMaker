package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.data.management.Person
import com.dgnt.quickTournamentMaker.data.tournament.Participant

class Data {
    companion object {

        //Person
        val ANDREW_PERSON = Person("Andrew", "")
        val KYRA_PERSON = Person("Kyra", "")
        val DGNT_PERSON = Person("DGNT", "")
        val KELSEY_PERSON = Person("Kelsey", "")
        val FIRE_PERSON = Person("Fire", "")
        val SUPER_PERSON = Person("Super", "")
        val HERO_PERSON = Person("Hero", "")
        val DEMON_PERSON = Person("Demon", "")

        //Participant
        val ANDREW = Participant(ANDREW_PERSON)
        val KYRA = Participant(KYRA_PERSON)
        val DGNT = Participant(DGNT_PERSON)
        val KELSEY = Participant(KELSEY_PERSON)
        val FIRE = Participant(FIRE_PERSON)
        val SUPER = Participant(SUPER_PERSON)
        val HERO = Participant(HERO_PERSON)
        val DEMON = Participant(DEMON_PERSON)
    }
}
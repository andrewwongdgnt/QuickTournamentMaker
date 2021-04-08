package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.SeedType

interface ISelectedPersonsService {


    /**
     * create a list of participants given a list of selected persons or a number of participants
     *
     * @param persons the persons selected to participate
     * @param numberOfParticipants the number of participants (unrelated to the number of persons selected)
     * @param quickStart whether the tournament is quick start or not
     * @param seedType the type of seeding
     * @param defaultParticipantNameFunc optional function to update the name of said participant. By default, it will just be itself
     * @return the list of names
     */
    fun resolve(persons: List<Person>?, numberOfParticipants: Int?, quickStart: Boolean, seedType: SeedType, defaultParticipantNameFunc: (Int) -> String = { p-> p.toString() }): List<Participant>
}
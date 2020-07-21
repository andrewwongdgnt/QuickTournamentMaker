package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.data.management.Person
import com.dgnt.quickTournamentMaker.data.tournament.Participant

interface ISeedService {
    /**
     * Generate the seeding
     *
     * @param people the list of persons
     * @return list of participants
     */
    fun seed(people: List<Person>) : List<Participant>
}
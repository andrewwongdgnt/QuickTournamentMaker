package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.service.interfaces.ISelectedPersonsService
import com.dgnt.quickTournamentMaker.util.shuffledIf

class SelectedPersonsService : ISelectedPersonsService {
    override fun resolve(persons: List<Person>?, numberOfParticipants: Int?, quickStart: Boolean, seedType: SeedType, defaultParticipantNameFunc: (Int) -> String): List<Participant> =
        //TODO this 3 magic number
        when {
            persons != null && persons.size >= 3 && !quickStart -> persons
            numberOfParticipants != null && numberOfParticipants >= 3 && quickStart -> (1..numberOfParticipants).map { Person("", defaultParticipantNameFunc(it), "") }
            else -> throw IllegalArgumentException("Bad parameters, cannot create")
        }.shuffledIf(seedType == SeedType.RANDOM).map { Participant(it) }.toList()


}
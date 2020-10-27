package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService

class ByePopulateSeedService  : ISeedService  {
    override fun seed(people: List<Person>): List<Participant> = (0 until people.size*2).map{if (it%2==0) Participant(people[it/2]) else Participant.BYE_PARTICIPANT}


}
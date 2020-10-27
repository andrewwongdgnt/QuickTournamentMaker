package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService

class EvenNumberSeedService  : ISeedService  {
    override fun seed(people: List<Person>): List<Participant> = (0 until if (people.size%2==0) people.size else people.size+1).map{if (it<people.size) Participant(people[it]) else Participant.BYE_PARTICIPANT}


}
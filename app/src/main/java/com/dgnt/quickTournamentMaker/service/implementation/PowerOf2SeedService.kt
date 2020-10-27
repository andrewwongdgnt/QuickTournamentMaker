package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService

class PowerOf2SeedService : ISeedService {
    override fun seed(people: List<Person>): List<Participant> {

        val nextPowerOf2 = nextPowerOf2(people.size)

        val indexToStartAddingByes = nextPowerOf2 - (nextPowerOf2 - people.size) * 2

        val peopleIterator = people.iterator()
        return (0 until nextPowerOf2).mapIndexed { i, _ -> if (i >= indexToStartAddingByes && i%2==1) Participant.BYE_PARTICIPANT else Participant(peopleIterator.next()) }

    }

    private fun nextPowerOf2(candidate: Int): Int {
        var n = candidate
        n--
        n = n or (n shr 1) // Divide by 2^k for consecutive doublings of k up to 32,
        n = n or (n shr 2) // and then or the results.
        n = n or (n shr 4)
        n = n or (n shr 8)
        n = n or (n shr 16)
        n++ // The result is a number of 1 bits equal to the number
        // of bits in the original number, plus 1. That's the
        // next highest power of 2.
        return n
    }

}
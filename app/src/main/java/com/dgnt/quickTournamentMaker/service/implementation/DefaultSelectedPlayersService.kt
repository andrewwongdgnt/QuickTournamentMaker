package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.service.interfaces.ISelectedPlayersService
import com.dgnt.quickTournamentMaker.util.shuffledIf

class DefaultSelectedPlayersService : ISelectedPlayersService {
    override fun resolve(names: List<String>?, numberOfPlayers: Int?, quickStart: Boolean, seedType: SeedType): List<String> =
        //TODO this 3 magic number
        when {
            names != null && names.size >= 3 && !quickStart -> names
            numberOfPlayers != null && numberOfPlayers >= 3 && quickStart -> (1..numberOfPlayers).map { it.toString() }
            else -> throw IllegalArgumentException("Bad parameters, cannot create player names")
        }.shuffledIf(seedType == SeedType.RANDOM).toList()


}
package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.service.interfaces.ISelectedPlayersService
import com.dgnt.quickTournamentMaker.util.shuffledIf

class DefaultSelectedPlayersService : ISelectedPlayersService {
    override fun resolve(names: List<String>?, numberOfPlayers: Int?, quickStart: Boolean, seedType: SeedType): List<String> =
        when {
            names != null && !quickStart -> names
            numberOfPlayers != null && quickStart -> (1..numberOfPlayers).map { it.toString() }
            else -> listOf()
        }.shuffledIf(seedType == SeedType.RANDOM).toList()


}
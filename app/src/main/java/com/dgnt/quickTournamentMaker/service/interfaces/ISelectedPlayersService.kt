package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.SeedType

interface ISelectedPlayersService {


    /**
     * create a list of names given a nullable list of names or nullable number of players
     *
     * @param names the nullable names
     * @param numberOfPlayers the nullable number of players
     * @param quickStart whether the tournament is quick start or not
     * @param seedType the type of seeding
     * @return the list of names
     */
    fun resolve(names: List<String>?, numberOfPlayers: Int?, quickStart: Boolean, seedType: SeedType): List<String>
}
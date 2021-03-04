package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.*

interface ITournamentInformationCreatorService {

    /**
     * Create a container for tournament information
     *
     * @param titleCandidate The title of the tournament
     * @param alternativeTitles the alternative titles in case the title candidate doesn't work. based on tournament type
     * @param description The description of the tournament
     * @param participants The participants chosen for the tournament
     * @param tournamentType the tournament type
     * @param seedType the seed type
     * @param rankConfig the rank config. may not matter for some types of tournaments
     * @return the tournament info container
     */
    fun create(titleCandidate: String, alternativeTitles: Map<TournamentType, String>, description: String, participants: List<Participant>, tournamentType: TournamentType, seedType: SeedType, rankConfig: IRankConfig): TournamentInformation
}
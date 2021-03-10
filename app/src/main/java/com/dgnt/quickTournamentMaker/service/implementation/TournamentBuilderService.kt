package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.data.TournamentTypeServices
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService

class TournamentBuilderService(private val services: Map<TournamentType, TournamentTypeServices>) : ITournamentBuilderService {
    override fun build(tournamentInfo: TournamentInformation) =
        services.getValue(tournamentInfo.tournamentType).run {
            val orderedParticipants = seedService.seed(tournamentInfo.participants)
            val roundGroups = roundGeneratorService.build(orderedParticipants)
            Tournament(tournamentInfo, roundGroups, roundUpdateService, rankingService, matchUpStatusTransformService)
        }

}


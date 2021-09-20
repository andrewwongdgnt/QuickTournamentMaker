package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.data.TournamentTypeServices
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService

class TournamentBuilderService(private val services: Map<TournamentType, TournamentTypeServices>) : ITournamentBuilderService {
    override fun build(tournamentInfo: TournamentInformation, orderedParticipants: List<Participant>, defaultRoundGroupTitleFunc: (Int) -> String, defaultRoundTitleFunc: (Int) -> String, defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String) =
        services.getValue(tournamentInfo.tournamentType).run {
            val roundGroups = roundGeneratorService.build(orderedParticipants, defaultRoundGroupTitleFunc, defaultRoundTitleFunc, defaultMatchUpTitleFunc)
            Tournament(tournamentInfo, roundGroups, roundUpdateService, rankingService, matchUpStatusTransformService)
        }

}


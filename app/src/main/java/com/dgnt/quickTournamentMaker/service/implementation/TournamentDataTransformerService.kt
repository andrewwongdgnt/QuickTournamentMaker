package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.data.serializable.MatchUpData
import com.dgnt.quickTournamentMaker.service.data.serializable.ParticipantData
import com.dgnt.quickTournamentMaker.service.data.serializable.RoundData
import com.dgnt.quickTournamentMaker.service.data.serializable.TournamentData
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentDataTransformerService

class TournamentDataTransformerService(
    private val rankingConfigService: IRankingConfigService
) : ITournamentDataTransformerService {
    override fun transform(tournament: Tournament) =
        tournament.run {
            TournamentData(
                2,
                tournamentInformation.title,
                tournamentInformation.description,
                tournamentInformation.tournamentType,
                when (tournamentInformation.tournamentType) {
                    TournamentType.ROUND_ROBIN, TournamentType.SWISS -> rankingConfigService.toString(tournamentInformation.rankConfig)
                    else -> null
                },
                tournamentInformation.seedType,
                tournamentInformation.creationDate,
                tournamentInformation.lastModifiedDate,
                orderedParticipants.map {
                    ParticipantData(
                        it.person.name,
                        it.name,
                        it.note,
                        it.participantType,
                        it.color,
                    )
                },
                rounds.map {
                    RoundData(
                        it.roundGroupIndex,
                        it.roundIndex,
                        it.originalTitle,
                        it.title,
                        it.note,
                        it.color,
                    )
                },
                matchUps.map {
                    MatchUpData(
                        it.roundGroupIndex,
                        it.roundIndex,
                        it.matchUpIndex,
                        it.title,
                        it.useTitle,
                        it.note,
                        it.color,
                        it.status,
                    )
                }
            )
        }

}
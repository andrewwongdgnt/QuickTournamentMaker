package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.FoundationalTournamentEntities
import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentRestoreService

class TournamentRestoreService : ITournamentRestoreService {
    override fun restore(tournament: Tournament, foundationalTournamentEntities: FoundationalTournamentEntities) {

        val roundMap = tournament.rounds.map { it.key to it }.toMap()
        foundationalTournamentEntities.roundEntities.forEach {
            roundMap[Pair(it.roundGroupIndex, it.roundIndex)]?.updateWith(it)
        }

        val matchUpMap = tournament.matchUps.map { it.key to it }.toMap()
        foundationalTournamentEntities.matchUpEntities.forEach {
            matchUpMap[Triple(it.roundGroupIndex, it.roundIndex, it.matchUpIndex)]?.updateWith(it)
        }

        val participantMap = tournament.sortedNormalParticipants.map { it.key to it }.toMap()
        foundationalTournamentEntities.participantEntities.forEach {
            participantMap[it.name + it.type]?.updateWith(it)
        }

        tournament.matchUps.forEach {
            tournament.updateRound(it.roundGroupIndex, it.roundIndex, it.matchUpIndex)
        }

    }
}
package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantPosition
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService

class OneWinnerMatchUpStatusTransformService : IMatchUpStatusTransformService {
    override fun transform(currentStatus: MatchUpStatus, participantPosition: ParticipantPosition): MatchUpStatus = when {
        participantPosition == ParticipantPosition.P1 && (currentStatus == MatchUpStatus.DEFAULT || currentStatus == MatchUpStatus.P2_WINNER) -> MatchUpStatus.P1_WINNER
        participantPosition == ParticipantPosition.P2 && (currentStatus == MatchUpStatus.DEFAULT || currentStatus == MatchUpStatus.P1_WINNER) -> MatchUpStatus.P2_WINNER
        else -> MatchUpStatus.DEFAULT
    }
}
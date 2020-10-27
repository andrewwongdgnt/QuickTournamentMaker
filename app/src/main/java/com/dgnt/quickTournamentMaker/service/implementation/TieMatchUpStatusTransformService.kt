package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantPosition
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService

class TieMatchUpStatusTransformService: IMatchUpStatusTransformService {
    override fun transform(currentStatus: MatchUpStatus, participantPosition: ParticipantPosition): MatchUpStatus = when {
        participantPosition == ParticipantPosition.P1 && currentStatus == MatchUpStatus.DEFAULT -> MatchUpStatus.P1_WINNER
        participantPosition == ParticipantPosition.P1 && currentStatus == MatchUpStatus.P1_WINNER -> MatchUpStatus.DEFAULT
        participantPosition == ParticipantPosition.P1 && currentStatus == MatchUpStatus.P2_WINNER -> MatchUpStatus.TIE
        participantPosition == ParticipantPosition.P1 && currentStatus == MatchUpStatus.TIE -> MatchUpStatus.P2_WINNER
        participantPosition == ParticipantPosition.P2 && currentStatus == MatchUpStatus.DEFAULT -> MatchUpStatus.P2_WINNER
        participantPosition == ParticipantPosition.P2 && currentStatus == MatchUpStatus.P1_WINNER -> MatchUpStatus.TIE
        participantPosition == ParticipantPosition.P2 && currentStatus == MatchUpStatus.P2_WINNER -> MatchUpStatus.DEFAULT
        participantPosition == ParticipantPosition.P2 && currentStatus == MatchUpStatus.TIE -> MatchUpStatus.P1_WINNER
        else -> MatchUpStatus.DEFAULT
    }
}
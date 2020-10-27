package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantPosition
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import com.dgnt.quickTournamentMaker.service.interfaces.IToggleStatusService


class ToggleStatusService (private val matchUpStatusTransformService: IMatchUpStatusTransformService) : IToggleStatusService {
    override fun toggle(matchUp: MatchUp, participantPosition: ParticipantPosition): Boolean {
        if (matchUp.participant1 == Participant.BYE_PARTICIPANT || matchUp.participant2 == Participant.BYE_PARTICIPANT) return false

        val currentStatus: MatchUpStatus = matchUp.status

        matchUp.status = matchUpStatusTransformService.transform(currentStatus,participantPosition)
        return true
    }
}
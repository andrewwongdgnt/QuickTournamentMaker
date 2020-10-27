package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.model.tournament.ParticipantPosition

interface IMatchUpStatusTransformService {

    /**
     * Return a new match up status given the current status and participant position
     *
     * @param currentStatus the current match up status
     * @param participantPosition the participant position
     * @return the new match up status
     */
    fun transform(currentStatus: MatchUpStatus, participantPosition: ParticipantPosition): MatchUpStatus
}
package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantPosition

interface IToggleStatusService {

    /**
     * Toggle the status of a given match up through the participant position
     *
     * @param matchUp the match up
     * @param participantPosition the participant position
     * @return whether the status had changed
     */
    fun toggle(matchUp: MatchUp, participantPosition: ParticipantPosition) : Boolean
}